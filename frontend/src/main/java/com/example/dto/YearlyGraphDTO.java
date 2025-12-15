package com.example.dto;

import java.util.List;

public record YearlyGraphDTO(
        int year,
        List<Long> data,
        String label
) {}