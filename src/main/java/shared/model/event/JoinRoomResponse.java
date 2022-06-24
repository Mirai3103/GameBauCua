package shared.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import shared.model.Room;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class JoinRoomResponse extends EventDataBase {
   private Room room;
}
