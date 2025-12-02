package dto;

import java.util.List;

public record FriendCardListDTO(
        List<FriendCardDTO> friends
){}