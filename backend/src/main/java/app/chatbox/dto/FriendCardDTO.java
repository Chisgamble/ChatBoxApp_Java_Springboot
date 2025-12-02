package app.chatbox.dto;

public record FriendCardDTO(
    long id,
    String username,
    InboxMsgDTO lastMsg
){
    public FriendCardDTO(FriendCardDTO dto, InboxMsgDTO lastMsg) {
        this(dto.id(), dto.username(), lastMsg);
    }
}
