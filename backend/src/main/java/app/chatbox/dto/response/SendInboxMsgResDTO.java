package app.chatbox.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendInboxMsgResDTO {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;

    public SendInboxMsgResDTO(Long id, Long senderId, Long receiverId, String content){
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
    }
}
