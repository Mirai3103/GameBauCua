package server.model;

import lombok.*;
import shared.model.event.GameResult;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoomResult {
    private int roomId;
    private Long roomOwnerId;
    private GameResult gameResult;
}
