package app.chatbox.dto;

public record ActivityDTO(
        Long userId,
        String username,
        String createdAt, // Account creation date/time (String for table display)
        Long openCount,
        Long chatOneCount,
        Long chatGroupCount
) {}