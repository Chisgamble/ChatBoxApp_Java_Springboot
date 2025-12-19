package app.chatbox.services;

import app.chatbox.dto.request.SendGroupMsgReqDTO;
import app.chatbox.dto.request.SendInboxMsgReqDTO;
import app.chatbox.dto.response.SendGroupMsgResDTO;
import app.chatbox.dto.response.SendInboxMsgResDTO;
import app.chatbox.model.*;
import app.chatbox.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepo;
    private final InboxRepository inboxRepo;
    private final InboxMsgRepository inboxMsgRepo;
    private final GroupMsgRepository groupMsgRepo;
    private final GroupMemberRepository groupMemberRepo;

    @Transactional
    public SendInboxMsgResDTO handleSendInboxMessage(Long senderId, SendInboxMsgReqDTO req){
        Inbox inbox = inboxRepo.findByUsers(senderId, req.getReceiverId())
                .orElseGet(() -> inboxRepo.create(senderId, req.getReceiverId()));

        AppUser sender = userRepo.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        InboxMsg msg = InboxMsg.builder()
                .inbox(inbox)
                .sender(sender)
                .content(req.getContent())
                .status("SENT")
                .build();

        InboxMsg inboxMsg = inboxMsgRepo.save(msg);
//        System.out.println("find" + inboxMsg.toString());
//
//        inbox.setLastMsg(inboxMsg);
        inboxRepo.save(inbox);

        return new SendInboxMsgResDTO(msg.getId(), senderId, sender.getUsername(), req.getReceiverId(), req.getContent());
    }

    @Transactional
    public SendGroupMsgResDTO handleSendGroupMsg(Long senderId, SendGroupMsgReqDTO req){
        Group group = new Group();
        group.setId(req.getGroupId());

        AppUser sender = userRepo.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        GroupMsg msg = GroupMsg.builder()
                .group(group)
                .sender(sender)
                .content(req.getContent())
                .build();

        groupMsgRepo.save(msg);

        return new SendGroupMsgResDTO(msg.getId(), req.getGroupId(), senderId, sender.getUsername(), req.getContent());
    }
}
