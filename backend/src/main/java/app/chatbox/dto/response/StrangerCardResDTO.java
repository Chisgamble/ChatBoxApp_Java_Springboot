package app.chatbox.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StrangerCardResDTO {
    private Long userId;
    private String username;
    private String initials;
    private boolean requestSent;

    public StrangerCardResDTO(Long userId, String username, boolean requestSent){
        this.userId = userId;
        this.username = username;
        this.initials = username.substring(0,1).toUpperCase();
        this.requestSent = requestSent;
    }
}