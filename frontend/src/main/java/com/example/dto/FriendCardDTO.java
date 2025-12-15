package com.example.dto;

//public record FriendCardDTO(
//    UserMiniDTO user,
//    InboxMsgDTO lastMsg
//){}

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendCardDTO {
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

    public String getInitials(){
        String[] words = username.split("\\s+");
        if (words.length > 1){
            return words[0].substring(0,1).toUpperCase() + words[1].substring(0,1).toUpperCase();
        }else{
            return words[0].substring(0,1).toUpperCase();
        }
    }

}
