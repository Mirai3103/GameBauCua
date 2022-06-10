package client.event;

import client.utils.GlobalVariable;
import client.view.Lobby;
import client.view.RoomForOwner;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shared.model.UserInfo;
import shared.model.event.ChatWorld;
import shared.model.event.CreateRoom;
import shared.model.event.EventPayload;
import shared.model.event.JoinRoomResponse;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@AllArgsConstructor
@NoArgsConstructor@Getter@Setter
public class EventHandler implements ChangeListener {
    private JFrame currentFrame;



    @Override
    public void stateChanged(ChangeEvent e) {

        EventPayload eventPayload = (EventPayload) e.getSource();
        System.out.println("EventHandler: " + eventPayload.getEventType());
        switch (eventPayload.getEventType()){
            case CHAT_WORLD : {
                if (currentFrame.getTitle().equalsIgnoreCase("lobby")){
                    Lobby lobby = (Lobby) currentFrame;
                    UserInfo from = eventPayload.getSender();
                    String message =((ChatWorld)eventPayload.getEventData()).getContent();
                    lobby.getChatPanel().getEnteredText().append(from.getUsername() + ": " + message + "\n");
                }
                ChatWorld chatWorld = (ChatWorld) eventPayload.getEventData();
                System.out.println(chatWorld.toString());
                break;
            }
            case JOIN_ROOM_RESPONSE: {
                JoinRoomResponse joinRoomResponse = (JoinRoomResponse) eventPayload.getEventData();
                System.out.println(joinRoomResponse.getRoom().toString());
                GlobalVariable.currentRoom = joinRoomResponse.getRoom();
                if(GlobalVariable.isRoomOwner()){
                    new RoomForOwner();
                }

                break;
            }
            default :{
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
}
