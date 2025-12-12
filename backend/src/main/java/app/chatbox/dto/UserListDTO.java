package app.chatbox.dto;

import java.util.List;

public record UserListDTO (
    List<UserDTO> users
){}
