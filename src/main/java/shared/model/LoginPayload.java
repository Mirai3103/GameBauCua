package shared.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shared.model.event.EventDataBase;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class LoginPayload extends EventDataBase {
    @Serial
    private static final long serialVersionUID = -6500665823330706018L;
    private String username;
    private String password;

}
