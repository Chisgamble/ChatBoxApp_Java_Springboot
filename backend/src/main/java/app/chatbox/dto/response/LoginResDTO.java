package app.chatbox.dto.response;


import app.chatbox.dto.UserMiniDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResDTO{
    UserMiniDTO user;
    String message;

    public LoginResDTO(UserMiniDTO user, String message){
        this.user = user;
        this.message = message;
    }
}