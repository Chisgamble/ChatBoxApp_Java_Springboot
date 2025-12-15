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
    private Long id;
    private Long friendId;
    private Long inboxId;
    private String username;
    private Boolean isActive;
    private Long senderId;
    private String last_msg;

    public FriendCardDTO(Long id, Long friendId, Long inboxId, String username, Boolean isActive, Long senderId, String last_msg){
        this.id = id;
        this.friendId = friendId;
        this.senderId = senderId;
        this.inboxId = inboxId;
        this.username = username;
        this.isActive = isActive;
        this.last_msg = last_msg;
    }
}