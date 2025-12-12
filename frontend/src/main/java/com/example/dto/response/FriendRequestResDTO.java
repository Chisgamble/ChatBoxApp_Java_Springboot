package com.example.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendRequestResDTO {
    private long id;
    private long senderId;
    private String senderUsername;
    private String status;

    public FriendRequestResDTO(long id, long senderId, String senderUsername, String status, boolean isActive) {
        this.id = id;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.status = status;
    }
}
