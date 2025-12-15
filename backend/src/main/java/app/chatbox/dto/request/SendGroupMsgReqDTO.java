package app.chatbox.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendGroupMsgReqDTO {
    private Long groupId;
    private String content;
}
