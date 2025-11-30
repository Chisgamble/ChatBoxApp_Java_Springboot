package app.chatbox.dto.response;

public record UserResDTO(
        Long id,
        String email,
        String initials,
        String role
) {}