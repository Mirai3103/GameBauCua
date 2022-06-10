package shared.model;

import lombok.Data;
import server.model.User;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = -6500665823330706018L;
    private String username;
    private String fullName;
    private Long id;
    private Long money;
    private Room currentRoom = null;
    public UserInfo(User user){
        this.username = user.getUsername();
        this.fullName = user.getFullName();
        this.id = user.getId();
        this.money = user.getMoney();
    }
    public void minusMoney(int money){
        this.money -= money;
    }
}
