package app.chatbox.controller;

import app.chatbox.config.CustomUserDetails;
import app.chatbox.dto.request.SendGroupMsgReqDTO;
import app.chatbox.dto.request.SendInboxMsgReqDTO;
import app.chatbox.dto.response.SendGroupMsgResDTO;
import app.chatbox.dto.response.SendInboxMsgResDTO;
import app.chatbox.model.Inbox;
import app.chatbox.repository.InboxRepository;
import app.chatbox.repository.UserRepository;
import app.chatbox.services.ChatService;
import app.chatbox.services.GroupMemberService;
import app.chatbox.services.InboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final InboxRepository inboxRepo;
    private final UserRepository userRepo;

    private final SimpMessagingTemplate messagingTemplate;
    private final SimpUserRegistry simpUserRegistry;

    @MessageMapping("/chat/inbox/send") // private 1-1 chat
    public void sendInboxMessage(
            @Payload SendInboxMsgReqDTO req,
            Principal principal){
        CustomUserDetails user =
                (CustomUserDetails) ((Authentication) principal).getPrincipal();

        Long senderId = user.getId();
        System.out.println("Name testing: " + principal.getName() + " - " + req.getInboxId());

        SendInboxMsgResDTO msg = chatService.handleSendInboxMessage(senderId, req);

        // lấy 2 user của inbox
        Inbox inbox = inboxRepo.findById(req.getInboxId())
                .orElseThrow(()->new RuntimeException("Inbox not found"));

        String userA = userRepo.findEmailById(user.getId());
        String userB = userRepo.findEmailById(req.getReceiverId());

        // gửi cho user A
        System.out.println("Sending to userA: " + userA);
        SimpUser userASession = simpUserRegistry.getUser(userA);
        if (userASession != null){
            messagingTemplate.convertAndSendToUser(
                    userA,
                    "/queue/inbox." + req.getInboxId(),
                    msg
            );
        }

        SimpUser userBSession = simpUserRegistry.getUser(userB);
        if (userBSession != null) {
            System.out.println("Sending to userB: " + userB);
            messagingTemplate.convertAndSendToUser(
                    userB,
                    "/queue/inbox." + req.getInboxId(),
                    msg
            );
        }
    }

    @MessageMapping("/chat/group/send") // group chat
    public void sendGroupMessage(
            @Payload SendGroupMsgReqDTO req,
            Principal principal){
        CustomUserDetails user =
                (CustomUserDetails) ((Authentication) principal).getPrincipal();

        Long senderId = user.getId();

        SendGroupMsgResDTO msg = chatService.handleSendGroupMsg(senderId, req);

        // gửi cho tất cả member
        String destination = "/topic/group/" + req.getGroupId();
        messagingTemplate.convertAndSend(
                "/topic/group." + req.getGroupId(),
                msg
        );

        // Tất cả client đang mở chat nhóm này và đã đăng ký kênh này sẽ nhận được tin nhắn.
    }
}
