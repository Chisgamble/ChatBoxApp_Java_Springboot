package app.chatbox.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Setter
public class MsgDTO {
    private Long id;
    private MsgType type;        // INBOX | GROUP

    private Long senderId;
    private String senderName;

    private Long inboxId;        // nullable
    private Long groupId;        // nullable
    private String groupName;    // nullable

    private String content;
    private LocalDateTime createdAt;

    public MsgDTO(
            Long id,
            String type,
            Long senderId,
            String senderName,
            Long inboxId,
            Long groupId,
            String groupName,
            String content,
            Timestamp createdAt
    ) {
        this.id = id;
        this.type = type != null ? MsgType.valueOf(type) : null;
        this.senderId = senderId;
        this.senderName = senderName;
        this.inboxId = inboxId;
        this.groupId = groupId;
        this.groupName = groupName;
        this.content = content;
        this.createdAt = createdAt != null
                ? createdAt.toLocalDateTime()
                : null;
    }
}