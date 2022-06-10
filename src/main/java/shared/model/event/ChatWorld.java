package shared.model.event;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ChatWorld extends EventDataBase {
    public static final Long serialVersionUID = -6500665823330706018L;
    private String content;
}
