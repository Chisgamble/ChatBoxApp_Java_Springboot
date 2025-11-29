package app.chatbox.mapper;

import app.chatbox.dto.FriendRequestDTO;
import app.chatbox.dto.response.FriendRequestResDTO;
import app.chatbox.model.FriendRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FriendRequestMapper {
    FriendRequestResDTO toDTO(FriendRequest entity);
    FriendRequest toEntity(FriendRequestDTO dto);
}