package com.example.dto.request;

import java.time.LocalDate;
import lombok.NoArgsConstructor;

public record AdminCreateOrUpdateUserReqDTO(
        String username,
        String name,
        String password,
        String gender,
        String address,
        String email,
        String birthday,
        String role
) {}