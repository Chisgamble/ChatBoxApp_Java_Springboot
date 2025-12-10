package app.chatbox.dto.response;

public record LoginResDTO(
    Long id,
    String email,
    String role,
    String message
) {}
