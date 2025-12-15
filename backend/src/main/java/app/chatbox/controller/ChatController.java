package app.chatbox.controller;

import app.chatbox.config.CustomUserDetails;
import app.chatbox.dto.request.SendGroupMsgReqDTO;
import app.chatbox.dto.request.SendInboxMsgReqDTO;
import app.chatbox.dto.response.SendGroupMsgResDTO;
import app.chatbox.dto.response.SendInboxMsgResDTO;
import app.chatbox.service.ChatService;
import app.chatbox.service.GroupMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final GroupMemberService groupMemberService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/send") // private 1-1 chat
    public void sendInboxMessage(@Payload SendInboxMsgReqDTO req, Principal principal){
        Long senderId = ((CustomUserDetails)((Authentication) principal).getPrincipal()).getId();

        SendInboxMsgResDTO msg = chatService.handleSendInboxMessage(senderId, req);

        messagingTemplate.convertAndSendToUser(
                req.getReceiverId().toString(),
                "/queue/chat",
                msg
        );
    }

    @MessageMapping("/chat/group/send") // group chat
    public void sendGroupMessage(@Payload SendGroupMsgReqDTO req, Principal principal){
        Long senderId = ((CustomUserDetails)((Authentication) principal).getPrincipal()).getId();

        SendGroupMsgResDTO msg = chatService.handleSendGroupMsg(senderId, req);

        // gửi cho tất cả member
        List<Long> memberIds = groupMemberService.getGroupMemberIds(req.getGroupId());
        for(Long memberId : memberIds){
            messagingTemplate.convertAndSendToUser(memberId.toString(), "/queue/group", msg);
        }
    }
}
