package dto;

public record FriendCardDTO(
    UserMiniDTO user,
    InboxMsgDTO lastMsg
){}
