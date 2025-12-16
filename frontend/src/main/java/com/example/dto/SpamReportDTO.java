package com.example.dto;

public record SpamReportDTO(
        Long id,
        Long reportedId,
        String reportedUsername,
        String reportedEmail,
        String reporterId,
        String createdAt,
        Boolean isLocked,
        String status
) {}