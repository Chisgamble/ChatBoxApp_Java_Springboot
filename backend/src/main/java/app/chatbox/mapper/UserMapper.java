package app.chatbox.mapper;

import app.chatbox.dto.FriendCardDTO;
import app.chatbox.dto.UserDTO;
import app.chatbox.dto.response.LoginResDTO;
import app.chatbox.dto.response.RegisterResDTO;
import app.chatbox.dto.response.UserResDTO;
import app.chatbox.model.AppUser;
import app.chatbox.model.Friend;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(AppUser entity);
    AppUser toEntity(UserDTO dto);
    UserResDTO toUserResDTO(AppUser entity);
    RegisterResDTO toRegisterResDTO(AppUser entity);
    LoginResDTO toLoginResDTO(AppUser entity);

    default UserDTO toAppUserDTO(AppUser user) {
        if (user == null) return null;

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                user.getGender(),
                user.getAddress(),
                user.getBirthday(),          // maps to dob
                null,                        // is_banned — not in AppUser, can set null or false
                user.getIsActive(),           // is_active
                user.getIs_locked(),          // is_locked
                user.getRole()
        );
    }

    default List<UserDTO> toAppUserDTOList(List<AppUser> users) {
        if (users == null) {
            return null;
        }
        return users.stream()
                .map(this::toAppUserDTO)  // pass each user to the mapper
                .toList();
    }

}