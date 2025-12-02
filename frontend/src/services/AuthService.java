package services;

import api.AuthApi;
import dto.request.LoginReqDTO;
import dto.request.RegisterReqDTO;
import dto.response.LoginResDTO;
import dto.response.RegisterResDTO;

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
