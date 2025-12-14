package app.chatbox.dto;

import java.time.LocalDateTime;

public record LoginLogDTO(
        Long id,
        String email,
        String username, // Added for UI display
        String name,     // Added for UI display
        boolean isSuccess,
        LocalDateTime createdAt
) {}