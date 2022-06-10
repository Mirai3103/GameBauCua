package shared.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class Room implements Serializable {
    private String roomName;
    private String roomPassword;
    private int roomMaxPlayer;
    private int roomId;
    private UserInfo roomOwner;
    private List<UserInfo> roomPlayers;
}
