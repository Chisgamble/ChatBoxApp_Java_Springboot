package com.example.dto.response;


import com.example.dto.UserMiniDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResDTO {
    UserMiniDTO user;
    String message;

    public LoginResDTO(UserMiniDTO user, String message){
        this.user = user;
        this.message = message;
    }
}