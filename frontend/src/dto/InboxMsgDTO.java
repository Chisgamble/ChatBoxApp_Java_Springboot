package dto;

public record InboxMsgDTO(
        long id,
        long senderId,
        long inboxId,
        String status,
        String content
) {}