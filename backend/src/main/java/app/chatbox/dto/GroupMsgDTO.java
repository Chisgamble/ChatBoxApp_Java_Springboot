package app.chatbox.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GroupMsgDTO{
    private Long id;
    private Long senderId;
    private String senderUsername;
    private String content;

    public GroupMsgDTO(Long id, Long senderId, String senderUsername, String content){
        this.id = id;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.content = content;
    }
}
