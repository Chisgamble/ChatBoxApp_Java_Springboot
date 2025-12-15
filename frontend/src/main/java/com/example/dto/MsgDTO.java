package com.example.dto;

import com.example.dto.MsgType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
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

    @JsonCreator
    public MsgDTO(
            @JsonProperty("id") Long id,
            @JsonProperty("type") String type,
            @JsonProperty("senderId") Long senderId,
            @JsonProperty("senderName") String senderName,
            @JsonProperty("inboxId") Long inboxId,
            @JsonProperty("groupId") Long groupId,
            @JsonProperty("groupName") String groupName,
            @JsonProperty("content") String content,
            @JsonProperty("createdAt") String createdAt
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
                ? LocalDateTime.parse(createdAt)
                : null;
    }
}