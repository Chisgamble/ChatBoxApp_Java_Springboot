package com.example.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class InboxMsgDTO implements BaseMsgDTO{
    private Long id;
    private Long senderId;
    private String senderName;
    private String status;
    private String content;

    public InboxMsgDTO(Long id, Long senderId, String senderName, String status, String content){
        this.id = id;
        this.senderId = senderId;
        this.senderName = senderName;
        this.status = status;
        this.content = content;
    }

    @Override
    public Long getId() { return id; }

    @Override
    public String getContent() { return content; }

    @Override
    public Long getSenderId() {
        return senderId;
    }

    @Override
    public String getSenderName() {
        return senderName;
    }
}
