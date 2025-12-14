package app.chatbox.dto.request;

import java.time.LocalDate;

public record AdminCreateOrUpdateUserReqDTO(
        String username,
        String password,
        String name,
        String email,
        String address,
        String gender,
        LocalDate birthday,
        String role
) {}