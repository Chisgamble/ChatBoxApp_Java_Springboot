package com.example.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateFriendRequestResDTO {
    private Long senderId;
    private Long receiverId;
    String status;

    public CreateFriendRequestResDTO(Long senderId, Long receiverId, String status){
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status = status;
    }
}
