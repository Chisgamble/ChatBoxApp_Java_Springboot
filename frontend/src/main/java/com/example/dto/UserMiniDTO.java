package com.example.dto;

public record UserMiniDTO(
        long id,
        String username,
        Boolean isActive
) {}
