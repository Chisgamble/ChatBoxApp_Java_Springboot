package app.chatbox.dto;

public record UserMiniDTO(
        long id,
        String username,
        Boolean isActive
) {}
