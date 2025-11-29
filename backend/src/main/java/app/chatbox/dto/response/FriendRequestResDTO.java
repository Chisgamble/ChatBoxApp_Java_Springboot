package app.chatbox.dto.response;

public record FriendRequestResDTO(
    long id,
    long senderId,
    String senderUsername,
    String status
) {}
