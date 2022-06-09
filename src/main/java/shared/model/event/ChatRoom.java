package shared.model.event;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ChatRoom extends EventDataBase {
    private String content;
    private Integer roomID;
}
