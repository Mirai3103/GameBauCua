package shared.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import shared.model.event.EventDataBase;


import java.util.List;
@Data
@AllArgsConstructor
public class Ranks extends EventDataBase {
    private List<UserInfo> ranks;

}
