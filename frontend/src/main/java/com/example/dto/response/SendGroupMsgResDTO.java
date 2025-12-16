package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendGroupMsgResDTO {
    private Long id;
    private Long groupId;
    private Long senderId;
    private String senderName;
    private String content;
}
