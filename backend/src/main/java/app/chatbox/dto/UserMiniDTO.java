package app.chatbox.dto;

public record UserMiniDTO(
        long id,
        String username,
        String initials,
        Boolean is_active
) {}
