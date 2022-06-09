package shared.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shared.model.UserInfo;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginReturned  implements Serializable {


    @Serial
    private static final long serialVersionUID = -6500665823330706018L;
    private String token;
    private UserInfo userInfo;
}
