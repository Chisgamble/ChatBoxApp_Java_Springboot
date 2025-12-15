package app.chatbox.dto;

import java.util.List;

public record NewUserListDTO(
        List<NewUserDTO> users
) {}