package dto.response;

import dto.InboxDTO;

import java.util.List;

public record LoginResDTO(
        UserResDTO user,
        List<InboxDTO> inboxes
) {}
