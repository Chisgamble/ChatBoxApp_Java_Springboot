package com.example.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserMiniDTO {
    private long id;
    private String email;
    private String username;
    private String role;
    private String initials;

    UserMiniDTO(long id, String email, String username, String role){
        this.id = id;
        this.email = email;
        this.username = username;
        this.role = role;
        this.initials = username.substring(0,1).toUpperCase();
    }
}