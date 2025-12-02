package app.chatbox.dto;

public record FriendCardDTO(
    UserMiniDTO user,
    InboxMsgDTO lastMsg
){}
