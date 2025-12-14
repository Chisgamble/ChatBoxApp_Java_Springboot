package app.chatbox.dto;

//public record FriendCardDTO(
//    UserMiniDTO user,
//    InboxMsgDTO lastMsg
//){}

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendCardDTO{
    private long id;
    private long senderId;
    private Long inboxId;
    private String username;
    private Boolean isActive;
    private String content;

    public FriendCardDTO(long id, long senderId, Long inboxId, String username, Boolean isActive, String content){
        this.id = id;
        this.senderId = senderId;
        this.inboxId = inboxId;
        this.username = username;
        this.isActive = isActive;
        this.content = content;
    }
}