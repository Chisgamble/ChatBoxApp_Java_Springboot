package com.example.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GroupMsgDTO implements BaseMsgDTO{
    private Long id;
    private Long senderId;
    private String senderUsername;
    private String content;

    public GroupMsgDTO(Long id, Long senderId, String senderUsername, String content){
        this.id = id;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.content = content;
    }

    @Override
    public Long getId() { return id; }

    @Override
    public Long getSenderId() {
        return senderId;
    }

    @Override
    public String getSenderName() {
        return senderUsername;
    }

    @Override
    public String getContent() { return content; }
}