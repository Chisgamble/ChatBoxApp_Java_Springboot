package com.example.dto;

import java.util.List;

public record SpamReportListDTO(
        List<SpamReportDTO> reports
) {}