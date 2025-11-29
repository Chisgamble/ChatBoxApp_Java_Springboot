package app.chatbox.dto.response;

public record UserResDTO(
        Long id,
        String email,
        String displayName,
        String role
) {}