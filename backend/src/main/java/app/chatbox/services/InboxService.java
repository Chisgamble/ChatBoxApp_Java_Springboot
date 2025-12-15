package app.chatbox.services;

import app.chatbox.dto.InboxDTO;
import app.chatbox.dto.InboxMsgDTO;
import app.chatbox.dto.UserMiniDTO;
import app.chatbox.dto.response.InboxUserResDTO;
import app.chatbox.mapper.InboxMapper;
import app.chatbox.model.AppUser;
import app.chatbox.model.Inbox;
import app.chatbox.repository.InboxMsgRepository;
import app.chatbox.repository.InboxRepository;
import app.chatbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InboxService {

    private final InboxRepository inboxRepo;
    private final InboxMsgRepository inboxMsgRepo;
    private final UserRepository userRepo;

    private final InboxMapper inboxMapper;

    private final InboxMsgService inboxMsgService;
    private final UserService userService;

    public InboxUserResDTO getUserInboxWithMsgs(Long userId, Long inboxId){
        Inbox inbox = inboxRepo.findById(inboxId)
                .orElseThrow(() -> new RuntimeException("Inbox not found"));

        Long friendId = (inbox.getUserA().getId() == userId) ? inbox.getUserB().getId() : inbox.getUserA().getId();

        UserMiniDTO friend = userService.findMiniById(friendId);
        List<InboxMsgDTO> msgs = inboxMsgRepo.findAllByInboxIdOrderByCreatedAt(inboxId);

        return new InboxUserResDTO(friend, msgs);
    }

    public List<InboxDTO> getInboxesWithLatestMsgs(AppUser user) {

        List<Inbox> inboxes = inboxRepo.findByUserA_IdOrUserB_Id(user.getId(),user.getId());

        return inboxes.stream().map(inbox -> {
            // convert entity → DTO (without messages first)
            InboxDTO dto = inboxMapper.toDTO(inbox);

            // load last 20 messages
            List<InboxMsgDTO> msgs = inboxMsgService.getLatestMessagesForInbox(inbox.getId());

            // return DTO with messages included
            return new InboxDTO(
                dto.id(),
                dto.userAId(),
                dto.userBId(),
                dto.userALastSeenMsgId(),
                dto.userBLastSeenMsgId(),
                msgs
            );
        }).toList();
    }

    @Transactional
    public void deleteAllMessages(Long inboxId) {
        inboxMsgRepo.deleteAllByInbox_Id(inboxId);
    }

}