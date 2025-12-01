package api;

import dto.request.LoginReqDTO;
import dto.request.RegisterReqDTO;
import dto.response.LoginResDTO;
import dto.response.RegisterResDTO;
import dto.response.UserResDTO;
import util.HttpClientUtil;
import util.JsonUtil;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;

public class AuthApi {

    private static final String BASE_URL = "http://localhost:8080/api/auth";
    private CookieManager cookieManager = new CookieManager();

    public AuthApi() {
        CookieHandler.setDefault(cookieManager);
    }

    public LoginResDTO login(LoginReqDTO dto) throws IOException {
        String url = BASE_URL + "/login";
        String json = JsonUtil.toJson(dto);
        LoginResDTO response = HttpClientUtil.postJson(url, dto, LoginResDTO.class);
        return response;
    }

    public RegisterResDTO register(RegisterReqDTO dto) throws IOException {
        String url = BASE_URL + "/register";
        String body = JsonUtil.toJson(dto);
        RegisterResDTO response = HttpClientUtil.postJson(url, dto, RegisterResDTO.class);
        return response;
    }
}
