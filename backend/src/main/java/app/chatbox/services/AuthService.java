package app.chatbox.services;


import app.chatbox.dto.response.LoginResDTO;

public interface AuthService {
    public LoginResDTO login(String email, String password);
}

