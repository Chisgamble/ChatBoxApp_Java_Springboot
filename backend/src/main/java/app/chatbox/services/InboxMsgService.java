package app.chatbox.services;

import app.chatbox.dto.InboxMsgDTO;
import app.chatbox.mapper.InboxMsgMapper;
import app.chatbox.model.AppUser;
import app.chatbox.model.Inbox;
import app.chatbox.repository.InboxMsgRepository;
import app.chatbox.repository.InboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InboxMsgService {

    private final InboxMsgRepository repo;
    private final InboxMsgMapper mapper;

    public List<InboxMsgDTO> getLatestMessagesForInbox(long inboxId) {

        return repo.findTop20ByInbox_IdOrderByCreatedAtDesc(inboxId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }
}
