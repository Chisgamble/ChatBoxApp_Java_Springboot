package app.chatbox.dto;

//setter, getter, equals, hashCode, toString
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String name;
    private String email;
    private Boolean isActive;
    private Boolean isLocked;
    private String role;
}
