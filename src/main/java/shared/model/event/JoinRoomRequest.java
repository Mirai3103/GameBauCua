package shared.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JoinRoomRequest extends EventDataBase {
   private int roomId;
    private String roomPassword;
}
