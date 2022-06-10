package shared.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class Bet extends EventDataBase{
    private int bet;
    private BetType betType;
    public enum BetType {
        BAU,
        CUA,
        TOM,
        CA,
        GA,
        NAI,
    }
}
