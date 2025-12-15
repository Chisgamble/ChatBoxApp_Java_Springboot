package com.example.dto;

public interface BaseMsgDTO {
    Long getId();
    Long getSenderId();
    String getSenderName();    // inbox có thể fake
    String getContent();
}
