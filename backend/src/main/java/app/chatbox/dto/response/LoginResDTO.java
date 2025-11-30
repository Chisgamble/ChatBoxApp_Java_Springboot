package app.chatbox.dto.response;

import app.chatbox.dto.InboxDTO;

import java.util.List;

public record LoginResDTO(
        UserResDTO user,
        List<InboxDTO> inboxes
) {}
