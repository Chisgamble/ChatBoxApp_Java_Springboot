package com.example.api;

import com.example.dto.request.ChatSuggestReqDTO;
import com.example.dto.response.ChatSuggestResDTO;
import com.example.util.HttpClientUtil;

public class ChatApi {

    private static final String BASE_URL = "http://localhost:8080/api/chat";

    public ChatSuggestResDTO getLLMSuggestion(ChatSuggestReqDTO req){
        String url = BASE_URL + "/suggest";
        return HttpClientUtil.postJson(
                url,
                req,
                ChatSuggestResDTO.class);
    }
}
