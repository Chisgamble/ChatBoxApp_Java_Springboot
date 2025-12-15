package app.chatbox.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InboxMsgDTO{
    private Long id;
    private Long senderId;
    private String senderName;
    private String status;
    private String content;

    public InboxMsgDTO(Long id, Long senderId, String senderName, String status, String content){
        this.id = id;
        this.senderId = senderId;
        this.senderName = senderName;
        this.status = status;
        this.content = content;
    }
}
