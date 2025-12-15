package app.chatbox.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendGroupMsgResDTO {
    private Long id;
    private Long groupId;
    private Long senderId;
    private String content;

    public SendGroupMsgResDTO(Long id, Long groupId, Long senderId, String content){
        this.id = id;
        this.groupId = groupId;
        this.senderId = senderId;
        this.content = content;
    }

}
