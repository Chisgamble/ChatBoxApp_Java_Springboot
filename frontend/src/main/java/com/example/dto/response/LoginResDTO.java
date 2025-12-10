package com.example.dto.response;

public record LoginResDTO(
    Long id,
    String email,
    String role,
    String message
) {}
