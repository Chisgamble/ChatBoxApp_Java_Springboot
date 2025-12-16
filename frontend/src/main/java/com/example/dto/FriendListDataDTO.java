package com.example.dto;

import java.time.Instant;

public record FriendListDataDTO(
        String username,
        long friendCount,
        long friendOfFriendCount,
        String createdAt
) {}