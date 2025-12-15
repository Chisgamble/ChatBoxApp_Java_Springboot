package com.example.dto;

public record NewUserDTO(
        String username,
        String email,
        String createdAt
) {}