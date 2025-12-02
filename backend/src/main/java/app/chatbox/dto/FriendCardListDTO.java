package app.chatbox.dto;

import java.util.List;

public record FriendCardListDTO(
    List<FriendCardDTO> friends
){}
