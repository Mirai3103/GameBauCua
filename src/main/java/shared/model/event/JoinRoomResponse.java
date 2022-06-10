package shared.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import shared.model.Room;

@AllArgsConstructor
@Getter
@Setter
public class JoinRoomResponse extends EventDataBase {
   private Room room;
}
