package app.chatbox.dto;

import java.time.Instant;

public record FriendRequestDTO(
   long id,
   long senderId,
   String senderUsername,
   String status,
   Instant createdAt
) {}
