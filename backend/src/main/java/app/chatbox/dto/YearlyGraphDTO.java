package app.chatbox.dto;

import java.util.List;

public record YearlyGraphDTO(
        int year,
        List<Long> data, // 12 numbers
        String label
) {}