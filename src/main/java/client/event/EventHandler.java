package client.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shared.model.event.ChatWorld;
import shared.model.event.EventPayload;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@AllArgsConstructor
@NoArgsConstructor@Getter@Setter
public class EventHandler implements ChangeListener {
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private JFrame currentFrame;



    @Override
    public void stateChanged(ChangeEvent e) {

        EventPayload eventPayload = (EventPayload) e.getSource();
        System.out.println("EventHandler: " + eventPayload.getEventType());
        switch (eventPayload.getEventType()){
            case CHAT_WORLD : {
                ChatWorld chatWorld = (ChatWorld) eventPayload.getEventData();
                System.out.println(chatWorld.toString());
                break;
            }
            default :{
                break;
            }
        }
    }
}
