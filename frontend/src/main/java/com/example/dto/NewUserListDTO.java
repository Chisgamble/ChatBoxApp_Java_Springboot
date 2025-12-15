package com.example.dto;

import java.util.List;

public record NewUserListDTO(
        List<NewUserDTO> users
) {}