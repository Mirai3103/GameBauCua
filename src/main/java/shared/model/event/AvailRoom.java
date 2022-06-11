package shared.model.event;

import lombok.*;
import shared.model.Room;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AvailRoom extends EventDataBase{
    private List<AvailRoomPayload> rooms = new ArrayList<>();

    public void add(Room room){
        rooms.add(new AvailRoomPayload(room));
    }
    @Getter
    @Setter
    @ToString
    public static class AvailRoomPayload extends EventPayload{
        private String roomName;
        private String roomPassword;
        private int roomMaxPlayer;
        private int roomId;
        private GameState.State roomState;
        public AvailRoomPayload(Room room){
            this.roomName = room.getRoomName();
            this.roomPassword = room.getRoomPassword();
            this.roomMaxPlayer = room.getRoomMaxPlayer();
            this.roomId = room.getRoomId();
            this.roomState = room.getRoomState();
        }
    }
}
