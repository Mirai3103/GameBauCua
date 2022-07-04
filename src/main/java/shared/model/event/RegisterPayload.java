package shared.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterPayload extends EventDataBase{
    private String fullName;
    private String userName;
    private String password;
}
