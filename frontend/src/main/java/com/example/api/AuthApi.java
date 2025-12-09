package com.example.api;

import com.example.dto.request.ChangePasswordReqDTO;
import com.example.dto.request.LoginReqDTO;
import com.example.dto.response.*;
import com.example.dto.request.RegisterReqDTO;
import com.example.util.HttpClientUtil;

public class AuthApi {

    private static final String BASE_URL = "http://localhost:8080/api/auth";

    public LoginResDTO login(LoginReqDTO dto){
        String url = BASE_URL + "/login";
        HttpClientUtil.resetCookieManager();
        LoginResDTO response = HttpClientUtil.postJson(url, dto, LoginResDTO.class);
        return response;
    }

    public RegisterResDTO register(RegisterReqDTO dto) {
        String url = BASE_URL + "/register";
        RegisterResDTO response = HttpClientUtil.postJson(url, dto, RegisterResDTO.class);
        return response;
    }

    public void logout() {
        String url = BASE_URL + "/logout";
        HttpClientUtil.get(url, LogoutResDTO.class);
        HttpClientUtil.resetCookieManager();
    }

    public GeneralResDTO changePassword(ChangePasswordReqDTO dto){
        String url = BASE_URL + "/change-password";
        return HttpClientUtil.postJson(url, dto, GeneralResDTO.class);
    }

    public GeneralResDTO resetPassword(ResetPasswordReqDTO dto){
        String url = BASE_URL + "/reset-password";
        return HttpClientUtil.postJson(url, dto, GeneralResDTO.class);
    }
}
