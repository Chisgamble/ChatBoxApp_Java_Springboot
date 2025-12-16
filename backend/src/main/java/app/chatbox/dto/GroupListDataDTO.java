package app.chatbox.dto;


import java.util.List;

public record GroupListDataDTO(
        List<GroupDataDTO> groups
) {}