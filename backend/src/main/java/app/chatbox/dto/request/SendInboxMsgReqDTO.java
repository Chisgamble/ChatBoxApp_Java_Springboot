package app.chatbox.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendInboxMsgReqDTO {
    private Long inboxId;
    private Long receiverId;
    private String content;
}