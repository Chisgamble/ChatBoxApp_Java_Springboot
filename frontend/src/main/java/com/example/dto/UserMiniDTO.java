package com.example.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class UserMiniDTO {
    private long id;
    private String email;
    private String username;
    private String role;
    private String initials = "";

    public UserMiniDTO(long id, String email, String username, String role){
        this.id = id;
        this.email = email;
        this.username = username;
        this.role = role;
        if (username != null && !username.isEmpty()) {
            this.initials = username.substring(0,1).toUpperCase();
        } else {
            this.initials = "U"; 
        }
    }

}