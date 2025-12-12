package com.example.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateFriendRequestResDTO {
    private long id;
    private long senderId;
    private String senderName;
    private Boolean isActive;

    public UpdateFriendRequestResDTO(long id, long senderId, String senderName, Boolean isActive){
        this.id = id;
        this.senderId = senderId;
        this.senderName = senderName;
        this.isActive = isActive;
    }
}
