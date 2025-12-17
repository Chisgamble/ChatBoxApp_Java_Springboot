package app.chatbox.controller;

import app.chatbox.dto.request.ChatSuggestReqDTO;
import app.chatbox.dto.response.ChatSuggestResDTO;
import app.chatbox.services.LlmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatSuggestController {

    private final LlmService llmService;

    @PostMapping("/suggest")
    public ChatSuggestResDTO suggest(@RequestBody ChatSuggestReqDTO req) {
        String result = llmService.suggest(req);
        return new ChatSuggestResDTO(result);
    }
}
