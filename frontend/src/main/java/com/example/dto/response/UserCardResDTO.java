package com.example.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCardResDTO {
    private Long userId;
    private String username;
    private String initials;

    public UserCardResDTO(Long userId, String username){
        this.userId = userId;
        this.username = username;
        this.initials = username.substring(0,1).toUpperCase();
    }
}