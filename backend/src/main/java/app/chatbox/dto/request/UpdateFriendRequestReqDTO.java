package app.chatbox.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateFriendRequestReqDTO {
    private long userId;
    private long senderId;
    String status;

    public UpdateFriendRequestReqDTO(long userId, long senderId, String status){
        this.userId = userId;
        this.senderId = senderId;
        this.status = status;
    }
}
