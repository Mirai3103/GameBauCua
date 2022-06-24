package shared.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import server.model.User;
import shared.model.event.EventDataBase;

@Getter
@Setter
@ToString
public class UserInfo extends EventDataBase {


    private String username;
    private String fullName;
    private Long id;
    private Long money;
    private int currentRoomId;
    public UserInfo(User user){
        this.username = user.getUsername();
        this.fullName = user.getFullName();
        this.id = user.getId();
        this.money = user.getMoney();
    }
    public void copy(UserInfo userInfo){
        this.username = userInfo.getUsername();
        this.fullName = userInfo.getFullName();
        this.id = userInfo.getId();
        this.money = userInfo.getMoney();
        this.currentRoomId = userInfo.getCurrentRoomId();
    }
    public void minusMoney(int money){
        this.money -= money;
    }
}
