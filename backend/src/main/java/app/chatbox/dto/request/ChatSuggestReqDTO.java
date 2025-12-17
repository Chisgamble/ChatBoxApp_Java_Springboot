package app.chatbox.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class ChatSuggestReqDTO{
    private Long inboxId;
    String currentInput;
    private List<String> recentMessages;
}
