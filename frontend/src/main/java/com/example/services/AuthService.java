package com.example.services;

import com.example.api.AuthApi;
import com.example.dto.request.LoginReqDTO;
import com.example.dto.request.RegisterReqDTO;
import com.example.dto.response.LoginResDTO;
import com.example.dto.response.RegisterResDTO;

public class AuthService {

    private final AuthApi api = new AuthApi();

    public RegisterResDTO register(String username, String email, String password) throws Exception {
        RegisterReqDTO dto = new RegisterReqDTO(username, email, password);
        return api.register(dto);
    }

    public LoginResDTO login(String email, String password) throws Exception {
        LoginReqDTO dto = new LoginReqDTO(email, password);
        return api.login(dto);
    }

    public void logout() {
        api.logout();
    }
}
