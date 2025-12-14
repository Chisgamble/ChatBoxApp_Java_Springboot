package app.chatbox.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserMiniDTO{
    private long id;
    private String email;
    private String username;
    private String role;
    private String initials;

    public UserMiniDTO(long id, String email, String username, String role){
        this.id = id;
        this.email = email;
        this.username = username;
        this.role = role;
        this.initials = username.substring(0,1).toUpperCase();
    }
}