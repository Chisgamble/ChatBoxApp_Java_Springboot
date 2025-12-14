package com.example.dto;

import com.example.dto.LoginLogDTO;

import java.util.List;

public record LoginLogListDTO(
        List<LoginLogDTO> logs
) {}