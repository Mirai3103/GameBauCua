package shared.model.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoom extends EventDataBase{

    private String roomPassword;
    private int roomMaxPlayer;

}
