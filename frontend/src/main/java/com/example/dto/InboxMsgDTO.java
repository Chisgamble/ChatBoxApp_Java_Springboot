package com.example.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class InboxMsgDTO {
    private Long id;
    private Long senderId;
    private String status;
    private String content;

    public InboxMsgDTO(Long id, Long senderId, String status, String content){
        this.id = id;
        this.senderId = senderId;
        this.status = status;
        this.content = content;
    }
}
