package shared.model;

import lombok.Data;
import server.model.User;

import java.io.Serializable;

@Data
public class UserInfo implements Serializable {
    private static final long serialVersionUID = -6500665823330706018L;
    private String username;
    private String fullName;
    private Long id;
    public UserInfo(User user){
        this.username = user.getUsername();
        this.fullName = user.getFullName();
        this.id = user.getId();
    }
}
