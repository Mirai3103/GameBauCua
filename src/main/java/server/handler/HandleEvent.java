package server.handler;

import client.view.RoomForOwner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import server.model.ClientSocket;
import server.model.RoomResult;
import server.model.User;
import server.utils.GlobalVariable;
import server.utils.HibernateUtils;
import shared.model.Room;
import shared.model.UserInfo;
import shared.model.event.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HandleEvent {
    public List<ClientSocket> clientSockets;

    public void handle(EventPayload event) throws IOException {
        System.out.println("HandleEvent: " + event.getEventType());
        switch (event.getEventType()) {
            case CHAT_WORLD -> {
                for (ClientSocket clientSocket : clientSockets) {
                    clientSocket.getObjectOutputStream().writeObject(event);
                }
                break;
            }
            case CREATE_ROOM -> {
                CreateRoom createRoom = (CreateRoom) event.getEventData();
                Room room = new Room();
                room.setRoomState(GameState.State.WAITING);
                room.setRoomId(GlobalVariable.sequenceId++);
                room.setRoomName("room" + room.getRoomId());
                room.setRoomOwner(event.getSender());
                room.setRoomPassword(createRoom.getRoomPassword());
                room.setRoomMaxPlayer(createRoom.getRoomMaxPlayer());
                room.setRoomPlayers(new ArrayList<>());
                room.getRoomOwner().setCurrentRoomId(room.getRoomId());
                room.getRoomPlayers().add(room.getRoomOwner());


                GlobalVariable.rooms.add(room);
                for (ClientSocket clientSocket : clientSockets) {
                    if (Objects.equals(clientSocket.getUser().getId(), event.getSender().getId())) {
                        clientSocket.getObjectOutputStream().writeObject(new EventPayload(EventPayload.EventType.JOIN_ROOM_RESPONSE, new JoinRoomResponse(room), event.getSender()));
                    }
                }
                break;
            }
            case JOIN_ROOM -> {
                JoinRoomRequest joinRoomRequest = (JoinRoomRequest) event.getEventData();
                Room room = GlobalVariable.rooms.stream().filter(room1 -> room1.getRoomId() == joinRoomRequest.getRoomId()).findFirst().orElse(null);

                if (room != null) {
                    if (room.getRoomState() == GameState.State.WAITING) {
                        if (room.getRoomPassword().equals(joinRoomRequest.getRoomPassword())) {
                            if (room.getRoomMaxPlayer() > room.getRoomPlayers().size()) {
                                room.getRoomPlayers().add(event.getSender());
                                for (ClientSocket clientSocket : clientSockets) {
                                    if (Objects.equals(clientSocket.getUser().getId(), event.getSender().getId())) {
                                        clientSocket.getUser().setCurrentRoomId(room.getRoomId());
                                        room.getRoomPlayers().add(clientSocket.getUser());
                                        clientSocket.getObjectOutputStream().writeObject(new EventPayload(EventPayload.EventType.JOIN_ROOM_RESPONSE, new JoinRoomResponse(room), event.getSender()));

                                    } else {
                                        clientSocket.getObjectOutputStream().writeObject(new EventPayload(EventPayload.EventType.HAS_A_USER_JOINED, new JoinRoomResponse(room), event.getSender()));
                                    }
                                }
                            } else {
                                for (ClientSocket clientSocket : clientSockets) {
                                    if (Objects.equals(clientSocket.getUser().getId(), event.getSender().getId())) {
                                        clientSocket.getObjectOutputStream().writeObject(new EventPayload(EventPayload.EventType.JOIN_ROOM_RESPONSE, new JoinRoomResponse(null), event.getSender()));
                                    }
                                }
                                break;
                            }
                        } else {
                            for (ClientSocket clientSocket : clientSockets) {
                                if (Objects.equals(clientSocket.getUser().getId(), event.getSender().getId())) {
                                    clientSocket.getObjectOutputStream().writeObject(new EventPayload(EventPayload.EventType.JOIN_ROOM_RESPONSE, new JoinRoomResponse(null), event.getSender()));
                                }
                            }
                            break;
                        }
                    }
                }
                break;
            }
            case END_GAME -> {
                UserInfo userInfo = event.getSender();
                BetList betList = (BetList) event.getEventData();
                System.out.println("HandleEvent: " + ((BetList) betList).getBets().toString());
                RoomResult rs = null;
                System.out.println("rs: " + GlobalVariable.roomResults.toString());
                System.out.println("rs: " + event.toString());
                for (RoomResult roomResult : GlobalVariable.roomResults) {
                    if (roomResult.getRoomId() == event.getSender().getCurrentRoomId()) {
                        rs = roomResult;
                        break;
                    }
                }
                int[] list = new int[6];
                if (rs != null) {
                    for (String r : rs.getGameResult().getResult()) {
                        switch (r) {
                            case "bau" -> {
                                list[0]++;

                            }
                            case "cua" -> {
                                list[1]++;

                            }
                            case "tom" -> {
                                list[2]++;

                            }
                            case "ca" -> {
                                list[3]++;

                            }
                            case "ga" -> {
                                list[4]++;

                            }
                            case "nai" -> {
                                list[5]++;

                            }

                        }
                    }
                }

                for (Bet bet : betList.getBets()) {
                    switch (bet.getBetType()) {
                        case BAU -> {
                            list[0]--;
                        }
                        case CUA -> {

                            list[1]--;

                        }
                        case TOM -> {

                            list[2]--;

                        }
                        case CA -> {

                            list[3]--;

                        }
                        case GA -> {

                            list[4]--;

                        }
                        case NAI -> {

                            list[5]--;

                        }

                    }
                }
                int sum = 0;
                for (int t : list) {
                    if (t < 0) {
                        sum -= 500;
                    }
                    if (t > 0) {
                        sum += (500 * t);
                    }
                    if (t == 0) {
                        sum += 500;
                    }
                }

                Session session = HibernateUtils.getSessionFactory().openSession();
                session.beginTransaction();
                User user = session.get(User.class, userInfo.getId());
                user.setMoney(user.getMoney() + sum);
                session.save(user);
                User owner = session.get(User.class, rs.getRoomOwnerId());
                owner.setMoney(owner.getMoney() - sum);

                session.save(owner);

                session.getTransaction().commit();
                session.close();
                System.out.println(" sum: " + sum);

                for (ClientSocket clientSocket : clientSockets) {
                    if (Objects.equals(userInfo.getId(), clientSocket.getUser().getId())) {
                        clientSocket.getObjectOutputStream().writeObject(new EventPayload(EventPayload.EventType.UPDATE_USER_INFO, new UserInfo(user), new UserInfo(user)));
                    }
                    if (Objects.equals(owner.getId(), clientSocket.getUser().getId())) {
                        clientSocket.getObjectOutputStream().writeObject(new EventPayload(EventPayload.EventType.UPDATE_USER_INFO, new UserInfo(owner), new UserInfo(owner)));

                    }
                }
                break;
            }
            case START_GAME -> {
                System.out.println("Start game: " + event.getEventData());
                GameResult gameResult = (GameResult) event.getEventData();
                RoomResult roomResult = null;
                for (RoomResult roomResults : GlobalVariable.roomResults) {
                    if (roomResult.getRoomId() == event.getSender().getCurrentRoomId()) {
                        roomResults.setGameResult(gameResult);
                        roomResult = roomResults;
                        break;
                    }
                }
                if (roomResult == null) {
                    roomResult = new RoomResult();
                    roomResult.setGameResult(gameResult);
                    roomResult.setRoomId(event.getSender().getCurrentRoomId());
                    roomResult.setRoomOwnerId(event.getSender().getId());
                    GlobalVariable.roomResults.add(roomResult);
                }

                for (Room room : GlobalVariable.rooms) {
                    if (room.getRoomId() == event.getSender().getCurrentRoomId()) {
                        room.setRoomState(GameState.State.PLAYING);
                        for (ClientSocket clientSocket : clientSockets) {
                            if (clientSocket.getUser().getCurrentRoomId() == room.getRoomId()) {
                                clientSocket.getObjectOutputStream().writeObject(event);
                            }
                        }
                    }
                }
                break;
            }
            case GET_AVAILABLE_ROOMS -> {
                UserInfo userInfo = event.getSender();
                AvailRoom availRoom = new AvailRoom();
                for (Room room : GlobalVariable.rooms) {
                    if (room.getRoomState() == GameState.State.WAITING) {
                        availRoom.add(room);
                    }
                }
                for (ClientSocket clientSocket : clientSockets) {
                    if (Objects.equals(clientSocket.getUser().getId(), event.getSender().getId())) {
                        clientSocket.getObjectOutputStream().writeObject(new EventPayload(EventPayload.EventType.RETURN_AVAILABLE_ROOMS, availRoom, event.getSender()));
                    }
                }
                break;
            }
            default -> {
                break;
            }
        }
    }
}
