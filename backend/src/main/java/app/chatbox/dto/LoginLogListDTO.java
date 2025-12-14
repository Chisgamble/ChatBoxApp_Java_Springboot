package app.chatbox.dto;

import java.util.List;

public record LoginLogListDTO(
        List<LoginLogDTO> logs
) {}