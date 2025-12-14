package app.chatbox.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InboxMsgDTO{
    private Long id;
    private Long senderId;
    private String status;
    private String content;

    public InboxMsgDTO(Long id, Long senderId, String status, String content){
        this.id = id;
        this.senderId = senderId;
        this.status = status;
        this.content = content;
    }
}
