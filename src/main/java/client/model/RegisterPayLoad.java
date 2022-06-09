package client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor@NoArgsConstructor@Data
public class RegisterPayLoad {
    private String fullName;
    private String userName;
    private String password;
    public  String toJson() {
        return "{\"fullName\":\"" + fullName + "\",\"userName\":\"" + userName + "\",\"password\":\"" + password + "\"}";
    }
}
