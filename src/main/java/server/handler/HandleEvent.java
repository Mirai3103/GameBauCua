package server.handler;

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
                room.setRoomId(GlobalVariable.sequenceId++);
                room.setRoomName("room" + room.getRoomId());
                room.setRoomOwner(event.getSender());
                room.setRoomPassword(createRoom.getRoomPassword());
                room.setRoomMaxPlayer(createRoom.getRoomMaxPlayer());
                room.setRoomPlayers(new ArrayList<>());
                room.getRoomPlayers().add(event.getSender());
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
                    if(room.getRoomPassword().equals(joinRoomRequest.getRoomPassword())){
                        if(room.getRoomMaxPlayer() > room.getRoomPlayers().size()){
                            room.getRoomPlayers().add(event.getSender());
                            for (ClientSocket clientSocket : clientSockets) {
                                if(Objects.equals(clientSocket.getUser().getId(), event.getSender().getId())){
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
                        }
                    }else{
                        for (ClientSocket clientSocket : clientSockets) {
                            if(Objects.equals(clientSocket.getUser().getId(), event.getSender().getId())){
                                clientSocket.getObjectOutputStream().writeObject(new EventPayload(EventPayload.EventType.JOIN_ROOM_RESPONSE,new JoinRoomResponse(null),event.getSender()));
                            }
                        }
                    }
                break;
            }}
            case BET: {
                UserInfo userInfo = event.getSender();
                BetList betList = (BetList) event.getEventData();
                for (Bet bet : betList.getBets()) {
                   userInfo.minusMoney(bet.getBet());
                }

                for (ClientSocket clientSocket : clientSockets) {
                    if(Objects.equals(userInfo.getCurrentRoom().getRoomOwner().getId(), clientSocket.getUser().getId())){
                        clientSocket.getObjectOutputStream().writeObject(event);
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
