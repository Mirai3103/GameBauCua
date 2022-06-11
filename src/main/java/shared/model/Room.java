package shared.model;

import client.view.RoomForOwner;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shared.model.event.GameState;

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
    private GameState.State roomState;
}
