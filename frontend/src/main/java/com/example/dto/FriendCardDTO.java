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
    private long inboxId;
    private String username;
    private Boolean isActive;
    private String content;

    // Constructor
    public FriendCardDTO(long id, long senderId, long inboxId, String username, Boolean isActive, String content) {
        this.id = id;
        this.senderId = senderId;
        this.inboxId = inboxId;
        this.username = username;
        this.isActive = isActive;
        this.content = content;
    }

    // Getters and Setters

    public void setId(long id) {
        this.id = id;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public void setInboxId(long inboxId) {
        this.inboxId = inboxId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
