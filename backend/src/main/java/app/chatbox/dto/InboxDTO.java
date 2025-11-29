package app.chatbox.dto;

public record InboxDTO(
        long id,
        long userA,
        long userB,
        long userA_last_seen,
        long userB_last_seen
) {}
