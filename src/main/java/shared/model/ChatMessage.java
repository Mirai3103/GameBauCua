package shared.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.model.User;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatMessage {
    private MessageType type;
    private String content;
    private User user;

    public enum MessageType {
        CHATROOM,
        CHATWORLD


    }
}
