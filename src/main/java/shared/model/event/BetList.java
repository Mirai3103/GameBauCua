package shared.model.event;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BetList extends EventDataBase {
    List<Bet> bets;
    public BetList() {
       bets = new ArrayList<>();
    }
    public void addBet(Bet bet) {
        if(bets.size() >=3) {
            bets.remove(0);
        }
        bets.add(bet);
    }
    public void clear() {
        bets.clear();
    }
}
