package com.example.api;

import com.example.dto.FriendCardDTO;
import com.example.dto.FriendListDataDTO;
import com.example.dto.response.GeneralResDTO;
import com.example.util.HttpClientUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FriendApi {
    private static final String BASE_URL = "http://localhost:8080/api/friend";

    public List<FriendCardDTO> getAllFriends(Long userId){
        String url = BASE_URL + "/" + userId.toString();
        List<FriendCardDTO> response = HttpClientUtil.get(url, new TypeReference<List<FriendCardDTO>>() {});
        return response;
    }
    public List<FriendCardDTO> getAllFriendsById(Long userId){
        String url = BASE_URL + "/by/" + userId.toString();
        List<FriendCardDTO> response = HttpClientUtil.get(url, new TypeReference<List<FriendCardDTO>>() {});
        return response;
    }


    public GeneralResDTO deleteFriend(Long friendId) {
        String url = BASE_URL + "/" + friendId;
        return HttpClientUtil.deleteJson(url, null, GeneralResDTO.class);
    }

    public List<FriendListDataDTO> getFriendListData(String username, String sortBy, String sortDir, String fcSymbol, Integer fcVal) {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL + "/getall/data?");

        if (username != null && !username.isEmpty()) {
            urlBuilder.append("username=").append(username).append("&");
        }

        if (sortBy != null && !sortBy.isEmpty()) {
            urlBuilder.append("sortBy=").append(sortBy).append("&");
        }

        if (sortDir != null && !sortDir.isEmpty()) {
            urlBuilder.append("sortDir=").append(sortDir).append("&");
        }

        // --- FIX: Explicitly encode fcSymbol to handle '<' or '>' ---
        if (fcSymbol != null && !fcSymbol.isEmpty() && fcVal != null) {
            try {
                // Encode the symbol here. e.g., ">" -> "%3E"
                String encodedSymbol = URLEncoder.encode(fcSymbol, StandardCharsets.UTF_8.toString());

                urlBuilder.append("fcSymbol=").append(encodedSymbol).append("&");
                urlBuilder.append("fcVal=").append(fcVal).append("&");
            } catch (Exception e) {
                // Handle or log the encoding failure, though highly unlikely with StandardCharsets.UTF_8
                e.printStackTrace();
            }
        }

        String finalUrl = urlBuilder.toString();

        // Clean up trailing characters
        if (finalUrl.endsWith("&") || finalUrl.endsWith("?")) {
            finalUrl = finalUrl.substring(0, finalUrl.length() - 1);
        }

        return HttpClientUtil.get(finalUrl, new TypeReference<List<FriendListDataDTO>>() {});
    }
}
