package server.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.model.ClientSocket;
import shared.model.event.ChatWorld;
import shared.model.event.EventPayload;

import java.io.IOException;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HandleEvent {
    public List<ClientSocket> clientSockets ;
    public void handle(EventPayload event) throws IOException {

        //toDo: check token
        event.setToken("token");
        switch (event.getEventType()){
            case CHAT_WORLD:{
                System.out.println(event.getEventData());
                for (ClientSocket clientSocket : clientSockets) {
                    clientSocket.getObjectOutputStream().writeObject(event);
                }
                break;
            }

            default:{
                break;
            }
        }
    }
}
