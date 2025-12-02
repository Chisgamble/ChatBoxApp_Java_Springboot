package api;

import dto.FriendCardDTO;
import dto.request.LoginReqDTO;
import dto.response.LoginResDTO;
import util.HttpClientUtil;

import java.util.List;

public class FriendApi {
    private static final String BASE_URL = "http://localhost:8080/api/friend";

    public List<FriendCardDTO> getAllFriends(Long userId){
        String url = BASE_URL + "/getAll";
        LoginResDTO response = HttpClientUtil.postJson(url, dto, LoginResDTO.class);
        return response;
    }
}
