package app.chatbox.mapper;

import app.chatbox.dto.UserDTO;
import app.chatbox.dto.response.RegisterResDTO;
import app.chatbox.dto.response.UserResDTO;
import app.chatbox.model.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(AppUser entity);
    AppUser toEntity(UserDTO dto);
    UserResDTO toUserResDTO(AppUser entity);
    RegisterResDTO toRegisterResDTO(AppUser entity);
}