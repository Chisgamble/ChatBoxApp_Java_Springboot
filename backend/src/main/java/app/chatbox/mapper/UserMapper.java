package app.chatbox.mapper;

import app.chatbox.dto.NewUserDTO;
import app.chatbox.dto.NewUserListDTO;
import app.chatbox.dto.UserDTO;
import app.chatbox.dto.response.LoginResDTO;
import app.chatbox.dto.response.RegisterResDTO;
import app.chatbox.dto.response.StrangerCardResDTO;
import app.chatbox.model.AppUser;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(AppUser entity);
    AppUser toEntity(UserDTO dto);
    StrangerCardResDTO toStrangerCardResDTO(AppUser entity);
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
                user.getIsLocked(),          // is_locked
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

    default NewUserDTO toNewUserDTO(AppUser user) {
        if (user == null) return null;

        // Convert Instant (UTC) to LocalDateTime (System Default)
        LocalDateTime createdLocal = null;
        if (user.getCreatedAt() != null) {
            createdLocal = LocalDateTime.ofInstant(
                    user.getCreatedAt(),
                    ZoneId.systemDefault()
            );
        }

        return new NewUserDTO(
                user.getUsername(),
                user.getEmail(),
                createdLocal
        );
    }

    default NewUserListDTO toNewUserListDTO(List<AppUser> users) {
        if (users == null) return null;

        List<NewUserDTO> dtos = users.stream()
                .map(this::toNewUserDTO)
                .toList();

        return new NewUserListDTO(dtos);
    }
}