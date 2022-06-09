package shared.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class LoginPayload implements Serializable {
    @Serial
    private static final long serialVersionUID = -6500665823330706018L;
    private String username;
    private String password;

}
