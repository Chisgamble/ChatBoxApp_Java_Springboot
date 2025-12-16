package app.chatbox.dto;

import java.util.List;

public record ActivityListDTO(
        List<ActivityDTO> activities
) {}