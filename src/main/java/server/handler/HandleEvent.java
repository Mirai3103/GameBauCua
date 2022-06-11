package server.handler;

import client.view.RoomForOwner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.model.ClientSocket;
import server.utils.GlobalVariable;
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
    public List<ClientSocket> clientSockets ;
    public void handle(EventPayload event) throws IOException {
        System.out.println("HandleEvent: " + event.getEventType());
        switch (event.getEventType()){
            case CHAT_WORLD:{
                for (ClientSocket clientSocket : clientSockets) {
                    clientSocket.getObjectOutputStream().writeObject(event);
                }
                break;
            }
            case CREATE_ROOM:{
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
                    if(Objects.equals(clientSocket.getUser().getId(), event.getSender().getId())){
                        clientSocket.getObjectOutputStream().writeObject(new EventPayload(EventPayload.EventType.JOIN_ROOM_RESPONSE,new JoinRoomResponse(room),event.getSender()));
                    }
                }
                break;
            }
            case JOIN_ROOM: {
                JoinRoomRequest joinRoomRequest = (JoinRoomRequest) event.getEventData();
                Room room = GlobalVariable.rooms.stream().filter(room1 -> room1.getRoomId() == joinRoomRequest.getRoomId()).findFirst().orElse(null);

                if(room != null){
                    if(room.getRoomState()== GameState.State.WAITING){
                    if(room.getRoomPassword().equals(joinRoomRequest.getRoomPassword())){
                        if(room.getRoomMaxPlayer() > room.getRoomPlayers().size()){
                            room.getRoomPlayers().add(event.getSender());
                            for (ClientSocket clientSocket : clientSockets) {
                                if(Objects.equals(clientSocket.getUser().getId(), event.getSender().getId())){
                                    clientSocket.getUser().setCurrentRoomId(room.getRoomId());
                                    room.getRoomPlayers().add(clientSocket.getUser());
                                    clientSocket.getObjectOutputStream().writeObject(new EventPayload(EventPayload.EventType.JOIN_ROOM_RESPONSE,new JoinRoomResponse(room),event.getSender()));

                                }else {
                                    clientSocket.getObjectOutputStream().writeObject(new EventPayload(EventPayload.EventType.HAS_A_USER_JOINED,new JoinRoomResponse(room),event.getSender()));
                                }
                            }
                        }else{
                            for (ClientSocket clientSocket : clientSockets) {
                                if(Objects.equals(clientSocket.getUser().getId(), event.getSender().getId())){
                                    clientSocket.getObjectOutputStream().writeObject(new EventPayload(EventPayload.EventType.JOIN_ROOM_RESPONSE,new JoinRoomResponse(null),event.getSender()));
                                }
                            }
                            break;
                        }
                    }else{
                        for (ClientSocket clientSocket : clientSockets) {
                            if(Objects.equals(clientSocket.getUser().getId(), event.getSender().getId())){
                                clientSocket.getObjectOutputStream().writeObject(new EventPayload(EventPayload.EventType.JOIN_ROOM_RESPONSE,new JoinRoomResponse(null),event.getSender()));
                            }
                        }
                        break;
                    }
                }
            }break;}
            case BET: {
                UserInfo userInfo = event.getSender();
                BetList betList = (BetList) event.getEventData();
                for (Bet bet : betList.getBets()) {
                   userInfo.minusMoney(bet.getBet());
                }

                for (ClientSocket clientSocket : clientSockets) {
                    if(userInfo.getCurrentRoomId() == clientSocket.getUser().getCurrentRoomId()){
                        clientSocket.getObjectOutputStream().writeObject(new EventPayload(EventPayload.EventType.BET,betList,event.getSender()));
                    }
                }
                break;
            }
            case START_GAME:{
                System.out.println("Start game: "+ event.getEventData());
               for (Room room : GlobalVariable.rooms) {
                   if(room.getRoomId() == event.getSender().getCurrentRoomId()){
                          room.setRoomState(GameState.State.PLAYING);
                          for (ClientSocket clientSocket : clientSockets) {
                            if(clientSocket.getUser().getCurrentRoomId() == room.getRoomId()){
                                 clientSocket.getObjectOutputStream().writeObject(event);
                            }}
                   }
               }
               break;
            }
            case GET_AVAILABLE_ROOMS: {
                UserInfo userInfo = event.getSender();
                AvailRoom availRoom = new AvailRoom();
                for (Room room : GlobalVariable.rooms) {
                    if(room.getRoomState() == GameState.State.WAITING){
                       availRoom.add(room);
                    }
                }
                for (ClientSocket clientSocket : clientSockets) {
                    if(Objects.equals(clientSocket.getUser().getId(), event.getSender().getId())){
                        clientSocket.getObjectOutputStream().writeObject(new EventPayload(EventPayload.EventType.RETURN_AVAILABLE_ROOMS,availRoom,event.getSender()));
                    }
                }
                break;
            }

            default:{
                break;
            }
        }
    }
}
