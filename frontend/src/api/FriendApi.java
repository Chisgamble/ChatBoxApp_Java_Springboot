package api;

import dto.FriendCardDTO;
import dto.FriendCardListDTO;
import util.HttpClientUtil;

public class FriendApi {
    private static final String BASE_URL = "http://localhost:8080/api/friend";

    public FriendCardListDTO getAllFriends(Long userId){
        String url = BASE_URL + "/" + userId.toString();
        FriendCardListDTO response = HttpClientUtil.get(url, FriendCardListDTO.class);
        return response;
    }
}
