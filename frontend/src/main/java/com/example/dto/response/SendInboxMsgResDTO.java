package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendInboxMsgResDTO {
    private Long id;
    private Long senderId;
    private String senderName;
    private Long receiverId;
    private String content;
}
