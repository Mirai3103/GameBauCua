package shared.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterResponse extends EventDataBase{
    private String message;
    private boolean isSuccess;
}
