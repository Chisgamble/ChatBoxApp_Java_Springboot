package app.chatbox.services;

import app.chatbox.dto.request.ChatSuggestReqDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class LlmService {

    private final WebClient webClient;

    public LlmService(WebClient.Builder builder, @Value("${cohere.api-key}") String apiKey) {
        this.webClient = builder
                .baseUrl("https://api.cohere.ai/v2")
                .defaultHeader(HttpHeaders.AUTHORIZATION,
                        "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String suggest(ChatSuggestReqDTO req) {

        String context = String.join("\n", req.getRecentMessages());

        String prompt = """
        You are a chat assistant.
        Conversation:
        %s
        
        User is typing:
        "%s"
        
        Suggest a short natural reply.
        
        TASK:
        - Generate ONE short natural reply.
        - Output ONLY the reply text.
        - DO NOT use quotation marks.
        - DO NOT add explanations.
        - DO NOT repeat the conversation.
        - DO NOT mention you are an AI.
        """.formatted(context, req.getCurrentInput());

        return suggestReply(prompt);
    }


    public String suggestReply(String prompt) {

        Map<String,Object> message = Map.of("role","user","content", prompt);

        Map<String,Object> body = Map.of(
                "model", "command-a-03-2025",
                "messages", List.of(message),
                "max_tokens", 100,
                "temperature", 0.7
        );

        Map<String,Object> res = webClient.post()
                .uri("/chat")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {})
                .block();

        Map<String,Object> msg = (Map<String,Object>) res.get("message");
        List<Map<String,Object>> content = (List<Map<String,Object>>) msg.get("content");
        return content.get(0).get("text").toString();
    }
}


