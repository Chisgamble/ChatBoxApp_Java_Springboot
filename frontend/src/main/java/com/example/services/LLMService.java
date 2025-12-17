package com.example.services;

import com.example.api.ChatApi;
import com.example.dto.request.ChatSuggestReqDTO;

import java.util.List;

public class LLMService {
    private ChatApi api = new ChatApi();

    public String getLLMSuggestion (Long inboxId, String currentInput, List<String> recentMsgs){
        ChatSuggestReqDTO req = new ChatSuggestReqDTO(inboxId, currentInput, recentMsgs);
        return api.getLLMSuggestion(req).getSuggestion();
    }

}
