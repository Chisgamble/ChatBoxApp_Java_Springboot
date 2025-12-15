package com.example.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateFriendRequestReqDTO {
    private Long senderId;
    private Long receiverId;

    public CreateFriendRequestReqDTO(Long senderId, Long receiverId){
        this.senderId = senderId;
        this.receiverId = receiverId;
    }
}
