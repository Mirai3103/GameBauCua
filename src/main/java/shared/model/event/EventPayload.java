package shared.model.event;

import lombok.*;
import shared.model.UserInfo;

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
    private UserInfo sender;

    public enum EventType {
        CHAT_WORLD,
        CHAT_ROOM,
        CHAT_USER,
        JOIN_ROOM,
        LEAVE_ROOM,
        CREATE_ROOM,
        JOIN_ROOM_RESPONSE,
        HAS_A_USER_JOINED,
        BET,
        START_GAME,
        GET_AVAILABLE_ROOMS,
        RETURN_AVAILABLE_ROOMS,
        END_GAME,
        UPDATE_USER_INFO,
        A_USER_LEFT, DISCONNECT, FAST_JOIN_ROOM, ROOM_NOT_AVAIL,


    }
}
