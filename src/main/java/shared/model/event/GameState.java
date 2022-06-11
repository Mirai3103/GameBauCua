package shared.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameState extends EventPayload {
    private State state;

    public enum State{
        WAITING,
        PLAYING,
        END
    }
}

