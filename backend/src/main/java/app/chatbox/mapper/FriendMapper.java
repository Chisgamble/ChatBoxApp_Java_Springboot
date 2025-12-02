package app.chatbox.mapper;

import app.chatbox.dto.FriendCardDTO;
import app.chatbox.dto.InboxMsgDTO;
import app.chatbox.model.AppUser;
import app.chatbox.model.Friend;
import app.chatbox.model.InboxMsg;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",  uses = InboxMsgMapper.class)
public interface FriendMapper {
    Friend toEntity(FriendCardDTO dto);
    InboxMsgDTO toDTO(InboxMsg lastMsg);

    default FriendCardDTO toFriendCardDTO(Friend friend, Long currentUserId) {
        AppUser otherUser = friend.getUserA().getId().equals(currentUserId)
                ? friend.getUserB()
                : friend.getUserA();
        return new FriendCardDTO(friend.getId(), otherUser.getUsername(), toDTO(friend.getLastMsg()));
    }

    default List<FriendCardDTO> toFriendCardDTOList(List<Friend> friends, Long currentUserId) {
        if (friends == null) {
            return null;
        }
        return friends.stream()
                .map(friend -> toFriendCardDTO(friend, currentUserId))
                .toList();
    }
}