package client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@Data
@ToString
@NoArgsConstructor
public class LoginPayLoad  implements Serializable {
    @Serial
    private static final long serialVersionUID = -6500665823330706018L;
    private String username;
    private String password;
    public String toJson() {
        return "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
    }
}
