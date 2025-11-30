package app.chatbox.services;

import app.chatbox.dto.InboxDTO;
import app.chatbox.dto.InboxMsgDTO;
import app.chatbox.mapper.InboxMapper;
import app.chatbox.model.AppUser;
import app.chatbox.model.Inbox;
import app.chatbox.repository.InboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InboxService {

    private final InboxRepository inboxRepo;
    private final InboxMapper inboxMapper;
    private final InboxMsgService inboxMsgService;

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
}