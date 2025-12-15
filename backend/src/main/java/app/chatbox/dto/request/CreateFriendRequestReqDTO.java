package app.chatbox.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFriendRequestReqDTO {
    private Long senderId;
    private Long receiverId;

    public CreateFriendRequestReqDTO(Long senderId, Long receiverId){
        this.senderId = senderId;
        this.receiverId = receiverId;
    }
}
