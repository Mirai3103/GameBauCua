package server.handler;

import client.view.RoomForOwner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.slf4j.Logger;
import server.model.ClientSocket;
import server.model.RoomResult;
import server.model.User;
import server.utils.GlobalVariable;
import server.utils.HibernateUtils;
import shared.model.Room;
import shared.model.UserInfo;
import shared.model.event.*;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor

@Data
public class HandleEvent {
    public List<ClientSocket> clientSockets;
    public ClientSocket ownerSocket ;
    public static Logger logger = org.slf4j.LoggerFactory.getLogger(LoginState.class);
    public HandleEvent(List<ClientSocket> clientSockets) {
        this.clientSockets = clientSockets;
    }

    public void handle(EventPayload event , ClientSocket ownSocket) throws IOException {
        this.ownerSocket = ownSocket;
        logger.info("User: " + event.getSender().getUsername() + " sent event " + event.getEventType());
        switch (event.getEventType()) {
            case CHAT_WORLD -> {
                for (ClientSocket clientSocket : clientSockets) {
                    clientSocket.getObjectOutputStream().writeObject(event);
                }
            }
            case DISCONNECT -> {
                ownSocket.getSocket().close();
                clientSockets.remove(ownSocket);
                logger.info("User: " + event.getSender().getUsername() + " disconnected");
            }
            case CREATE_ROOM -> {
                CreateRoom createRoom = (CreateRoom) event.getEventData();
                Room room = new Room();
                room.setRoomState(GameState.State.WAITING);
                room.setRoomId(GlobalVariable.sequenceId++);
                room.setRoomName("Room " + room.getRoomId());
                room.setRoomOwner(event.getSender());
                room.setRoomPassword(createRoom.getRoomPassword());
                room.setRoomMaxPlayer(createRoom.getRoomMaxPlayer());
                room.setRoomPlayers(new ArrayList<>());
                room.getRoomOwner().setCurrentRoomId(room.getRoomId());
                room.getRoomPlayers().add(room.getRoomOwner());


                GlobalVariable.rooms.add(room);
                logger.info("User: " + event.getSender().getUsername() + " created room " + room.getRoomName());
                ownSocket.getObjectOutputStream().writeObject(new EventPayload(EventPayload.EventType.JOIN_ROOM_RESPONSE, new JoinRoomResponse(room), event.getSender()));



            }
            case JOIN_ROOM -> {
                JoinRoomRequest joinRoomRequest = (JoinRoomRequest) event.getEventData();
                Room room = GlobalVariable.rooms.stream().filter(room1 -> room1.getRoomId() == joinRoomRequest.getRoomId()).findFirst().orElse(null);

               joinRoom(room, event.getSender());
            }
            case LEAVE_ROOM -> {
                UserInfo userInfo = (UserInfo) event.getSender();
                Room room = GlobalVariable.rooms.stream().filter(room1 -> room1.getRoomId() == userInfo.getCurrentRoomId()).findFirst().orElse(null);
                if (room != null) {
                    room.getRoomPlayers().stream().filter(user -> Objects.equals(user.getId(), userInfo.getId())).findFirst().ifPresent(user -> {
                        room.getRoomPlayers().remove(user);
                    });

                    for (ClientSocket clientSocket : clientSockets) {
                        if (Objects.equals(clientSocket.getUser().getId(), event.getSender().getId())) {
                            clientSocket.getUser().setCurrentRoomId(-10);
                            break;
                        }
                        clientSocket.getObjectOutputStream().writeObject(new EventPayload(EventPayload.EventType.A_USER_LEFT, null, userInfo));
                    }
                    logger.info("User: " + event.getSender().getUsername() + " left room " + room.getRoomName());
                }
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

            }
            case FAST_JOIN_ROOM -> {
                UserInfo userInfo = event.getSender();
                for (Room room : GlobalVariable.rooms) {
                    if (room.getRoomState() == GameState.State.WAITING) {
                        // toDo: check password and max player
                       joinRoom(room, userInfo);
                       return;
                    }
                }
                joinRoom(null, userInfo);


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

            }

        }
    }

    public void joinRoom(Room room, UserInfo userInfo) throws IOException {
        if (room != null) {
            if (room.getRoomState() == GameState.State.WAITING) {
                room.getRoomPlayers().add(userInfo);
                for (ClientSocket clientSocket : clientSockets) {
                    if (Objects.equals(clientSocket.getUser().getId(), userInfo.getId())) {
                        clientSocket.getUser().setCurrentRoomId(room.getRoomId());
                        room.getRoomPlayers().add(clientSocket.getUser());
                        clientSocket.getObjectOutputStream().writeObject(new EventPayload(EventPayload.EventType.JOIN_ROOM_RESPONSE, new JoinRoomResponse(room), userInfo));

                    } else {
                        clientSocket.getObjectOutputStream().writeObject(new EventPayload(EventPayload.EventType.HAS_A_USER_JOINED, userInfo, userInfo));
                    }
                }
                logger.info("User: " + userInfo.getUsername() + " joined room " + room.getRoomName());
            }
        }else {
            logger.info("User: " + userInfo.getUsername() + " failed to join room");
            ownerSocket.getObjectOutputStream().writeObject(new EventPayload(EventPayload.EventType.ROOM_NOT_AVAIL, null, userInfo));
        }
    }
}
