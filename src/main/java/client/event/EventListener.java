package client.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shared.model.event.EventPayload;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class EventListener extends Thread{
    private ObjectInputStream objectInputStream;
    protected List<ChangeListener> listeners;
    public EventListener(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
        this.listeners = new java.util.ArrayList<>();
    }
    public void addListener(ChangeListener listener) {
        listeners.add(listener);
    }
    @Override
    public void run() {
        while (true) {
            try {
                EventPayload event = (EventPayload) objectInputStream.readObject();
                System.out.println("EventListener: " + event.getEventType());
                for (ChangeListener listener : listeners) {
                    listener.stateChanged(new ChangeEvent(event));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
