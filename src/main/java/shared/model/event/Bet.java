package shared.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
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
