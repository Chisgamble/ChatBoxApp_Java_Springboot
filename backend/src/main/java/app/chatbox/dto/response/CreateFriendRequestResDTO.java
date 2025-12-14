package app.chatbox.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
