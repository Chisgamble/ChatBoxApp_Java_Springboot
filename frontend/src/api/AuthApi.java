package api;

import dto.request.LoginReqDTO;
import dto.response.LogoutResDTO;
import dto.request.RegisterReqDTO;
import dto.response.LoginResDTO;
import dto.response.RegisterResDTO;
import util.HttpClientUtil;

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
}
