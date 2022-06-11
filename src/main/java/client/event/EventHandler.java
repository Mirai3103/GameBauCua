package client.event;

import client.utils.GlobalVariable;
import client.view.DialogListRoom;
import client.view.Lobby;
import client.view.RoomForOwner;
import client.view.RoomForPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shared.model.UserInfo;
import shared.model.event.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventHandler implements ChangeListener {
    private JFrame currentFrame;


    @Override
    public void stateChanged(ChangeEvent e) {

        EventPayload eventPayload = (EventPayload) e.getSource();
        System.out.println("EventHandler: " + eventPayload.getEventType());
        switch (eventPayload.getEventType()) {
            case CHAT_WORLD: {
                if (currentFrame.getTitle().equalsIgnoreCase("lobby")) {
                    Lobby lobby = (Lobby) currentFrame;
                    UserInfo from = eventPayload.getSender();
                    String message = ((ChatWorld) eventPayload.getEventData()).getContent();
                    lobby.getChatPanel().getEnteredText().append(from.getUsername() + ": " + message + "\n");
                }
                ChatWorld chatWorld = (ChatWorld) eventPayload.getEventData();
                System.out.println(chatWorld.toString());
                break;
            }
            case JOIN_ROOM_RESPONSE: {
                JoinRoomResponse joinRoomResponse = (JoinRoomResponse) eventPayload.getEventData();
                GlobalVariable.currentRoom = joinRoomResponse.getRoom();
                GlobalVariable.userInfo.setCurrentRoomId(joinRoomResponse.getRoom().getRoomId());
                if (GlobalVariable.isRoomOwner()) {
                    new RoomForOwner();
                } else {
                    new RoomForPlayer();
                }

                break;
            }
            case BET: {
                BetList betList = (BetList) eventPayload.getEventData();
                if (GlobalVariable.isRoomOwner()) {
                    RoomForOwner roomForOwner = (RoomForOwner) currentFrame;
                    betList.getBets().forEach(bet -> {
                        switch (bet.getBetType()) {
                            case BAU -> roomForOwner.plusBauBet(bet.getBet());
                            case CUA -> roomForOwner.plusCuaBet(bet.getBet());
                            case TOM -> roomForOwner.plusTomBet(bet.getBet());
                            case CA -> roomForOwner.plusCaBet(bet.getBet());
                            case GA -> roomForOwner.plusGaBet(bet.getBet());
                            case NAI -> roomForOwner.plusNaiBet(bet.getBet());
                            default -> System.out.println("Error");
                        }
                    });
                    roomForOwner.reDrawButton();
                }
            }
            case RETURN_AVAILABLE_ROOMS: {
                AvailRoom rooms = (AvailRoom) eventPayload.getEventData();
                System.out.println(rooms.toString());
                String id = new DialogListRoom(currentFrame, rooms).run();
                System.out.println(id);
                if (id != null) {
                    try {
                        joinRoom(Integer.parseInt(id));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                break;
            }
            case START_GAME: {
                if (!GlobalVariable.isRoomOwner()) {
                    RoomForPlayer roomForPlayer = (RoomForPlayer) currentFrame;
                    try {
                        roomForPlayer.setGameState(GameState.State.PLAYING);
                        roomForPlayer.setResult(((GameResult)eventPayload.getEventData()).getResult());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    public void createRoom(CreateRoom room) throws IOException {
        EventPayload eventPayload = new EventPayload();
        eventPayload.setEventType(EventPayload.EventType.CREATE_ROOM);
        eventPayload.setEventData(room);
        eventPayload.setSender(GlobalVariable.userInfo);
        GlobalVariable.objectOutputStream.writeObject(eventPayload);

    }

    public void startGame(String[] xucXac) throws IOException {
        EventPayload eventPayload = new EventPayload();
        eventPayload.setEventType(EventPayload.EventType.START_GAME);
        eventPayload.setEventData(new GameResult(xucXac));
        eventPayload.setSender(GlobalVariable.userInfo);
        GlobalVariable.objectOutputStream.writeObject(eventPayload);
    }

    public void joinRoom(int roomId) throws IOException {
        EventPayload eventPayload = new EventPayload();
        eventPayload.setEventType(EventPayload.EventType.JOIN_ROOM);
        eventPayload.setEventData(new JoinRoomRequest(roomId, ""));
        eventPayload.setSender(GlobalVariable.userInfo);
        GlobalVariable.objectOutputStream.writeObject(eventPayload);
    }

    public void getListAvailableRoom() throws IOException {
        EventPayload eventPayload = new EventPayload();
        eventPayload.setEventType(EventPayload.EventType.GET_AVAILABLE_ROOMS);
        eventPayload.setSender(GlobalVariable.userInfo);
        GlobalVariable.objectOutputStream.writeObject(eventPayload);
    }
}
