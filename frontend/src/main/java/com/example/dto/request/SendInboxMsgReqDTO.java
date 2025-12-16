package com.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendInboxMsgReqDTO {
    private Long inboxId;
    private Long receiverId;
    private String content;
}