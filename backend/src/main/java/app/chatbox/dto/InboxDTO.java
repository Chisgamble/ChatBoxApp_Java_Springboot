package app.chatbox.dto;

import java.util.List;

public record InboxDTO(
        long id,
        long userAId,
        long userBId,
        long userALastSeenMsgId,
        long userBLastSeenMsgId,
        List<InboxMsgDTO> latestMsgs
) {}
