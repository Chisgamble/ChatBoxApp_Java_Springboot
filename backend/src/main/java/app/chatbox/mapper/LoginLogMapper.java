package app.chatbox.mapper;

import app.chatbox.dto.LoginLogDTO;
import app.chatbox.model.AppUser;
import app.chatbox.model.LoginLog;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LoginLogMapper {

    default LoginLogDTO toDTO(LoginLog log, AppUser user) {
        if (log == null) return null;

        return new LoginLogDTO(
                log.getId(),
                log.getEmail(),
                user != null ? user.getUsername() : "Unknown", // Handle missing users gracefully
                user != null ? user.getName() : "Unknown",
                log.isSuccess(),
                log.getCreatedAt()
        );
    }
}