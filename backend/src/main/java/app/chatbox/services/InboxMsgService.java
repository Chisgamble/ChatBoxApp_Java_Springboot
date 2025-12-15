package app.chatbox.services;

import app.chatbox.dto.InboxMsgDTO;
import app.chatbox.dto.request.DeleteMsgsBySenderReq;
import app.chatbox.mapper.InboxMsgMapper;
import app.chatbox.model.Inbox;
import app.chatbox.model.InboxMsg;
import app.chatbox.repository.InboxMsgRepository;
import app.chatbox.repository.InboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InboxMsgService {

    private final InboxMsgRepository repo;
    private final InboxRepository inboxRepo;

    private final InboxMsgMapper mapper;

    public List<InboxMsgDTO> getLatestMessagesForInbox(long inboxId) {

        return repo.findTop20ByInbox_IdOrderByCreatedAtDesc(inboxId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Transactional
    public void deleteMessagesBySenderId(Long inboxId, DeleteMsgsBySenderReq req, Long authUserId) {

        repo.deleteByIdInAndSender_Id(req.getMsgIds(), authUserId);

        // Update last_msg
        Inbox inbox = inboxRepo.findById(inboxId)
                .orElseThrow(() -> new RuntimeException("Inbox not found"));

        InboxMsg latestMsg = repo.findTopByInbox_IdOrderByCreatedAtDesc(inboxId)
                .orElse(null);

        inbox.setLastMsg(latestMsg);
    }
}
