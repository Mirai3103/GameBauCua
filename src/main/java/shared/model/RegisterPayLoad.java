package shared.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor@Data
public class RegisterPayLoad {
    private String userName;
    private String password;
    private String fullName;

}
