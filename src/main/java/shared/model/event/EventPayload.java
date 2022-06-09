package shared.model.event;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class EventPayload implements Serializable {
    public static final Long serialVersionUID = 1L;
    private EventType eventType;
    private EventDataBase eventData;
    private String token;

    public String getToken() {
        return this.token;
    }


    public void setToken(String token) {
        this.token = token;
    }

    public enum EventType {
        CHAT_WORLD,
        CHAT_ROOM,
        CHAT_USER,
        JOIN_ROOM,
        LEAVE_ROOM,
        CREATE_ROOM,

    }
}
