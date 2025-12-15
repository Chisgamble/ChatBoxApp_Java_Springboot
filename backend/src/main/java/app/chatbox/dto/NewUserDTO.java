package app.chatbox.dto;

import java.time.LocalDateTime;

public record NewUserDTO(
        String username,
        String email,
        LocalDateTime createdAt
) {}