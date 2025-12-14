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
    private long id;
    private long senderId;
    private Long inboxId;
    private String username;
    private Boolean isActive;
    private String content;

    // Constructor
    public FriendCardDTO(long id, long senderId, Long inboxId, String username, Boolean isActive, String content) {
        this.id = id;
        this.senderId = senderId;
        this.inboxId = inboxId;
        this.username = username;
        this.isActive = isActive;
        this.content = content;
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
