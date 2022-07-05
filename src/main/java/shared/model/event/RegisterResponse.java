package shared.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterResponse extends EventDataBase{
    private String message;
    private boolean isSuccess;
}
