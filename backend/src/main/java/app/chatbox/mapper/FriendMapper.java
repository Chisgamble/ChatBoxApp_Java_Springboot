package app.chatbox.mapper;

import app.chatbox.dto.FriendCardDTO;
import app.chatbox.dto.InboxMsgDTO;
import app.chatbox.dto.UserMiniDTO;
import app.chatbox.model.AppUser;
import app.chatbox.model.Friend;
import app.chatbox.model.InboxMsg;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FriendMapper {
    Friend toEntity(FriendCardDTO dto);
    InboxMsgDTO toDTO(InboxMsg lastMsg);

//    @Mapping(target = "initials", expression = "java(getInitials(user))")
//    UserMiniDTO toDTO(AppUser user);
//
//    default String getInitials(AppUser user) {
//        if (user.getUsername() == null || user.getUsername().isEmpty()) return "";
//        return user.getUsername().substring(0, 1).toUpperCase();
//    }
//
//    default FriendCardDTO toFriendCardDTO(Friend friend, Long currentUserId) {
//        AppUser otherUser = friend.getUserA().getId().equals(currentUserId)
//                ? friend.getUserB()
//                : friend.getUserA();
//        return new FriendCardDTO(toDTO(otherUser), toDTO(friend.getLastMsg()));
//    }
//
//    default List<FriendCardDTO> toFriendCardDTOList(List<Friend> friends, Long currentUserId) {
//        if (friends == null) {
//            return null;
//        }
//        return friends.stream()
//                .map(friend -> toFriendCardDTO(friend, currentUserId))
//                .toList();
//    }
}