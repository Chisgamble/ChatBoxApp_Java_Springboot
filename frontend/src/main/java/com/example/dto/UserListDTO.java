package com.example.dto;

import java.util.List;

public record UserListDTO (
        List<UserDTO> users
){}
