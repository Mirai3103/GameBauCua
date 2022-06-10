package client.event;

import client.utils.GlobalVariable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shared.model.event.EventPayload;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class EventListener extends Thread{

    protected List<ChangeListener> listeners  = new ArrayList<>();
    public void addListener(ChangeListener listener) {
        listeners.add(listener);
    }
    @Override
    public void run() {
        while (true) {
            try {
                EventPayload event = (EventPayload)  GlobalVariable.objectInputStream.readObject();

                for (ChangeListener listener : listeners) {
                    listener.stateChanged(new ChangeEvent(event));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
