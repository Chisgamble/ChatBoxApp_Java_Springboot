package app.chatbox.controller;

import app.chatbox.config.CustomUserDetails;
import app.chatbox.dto.InboxMsgDTO;
import app.chatbox.dto.request.CreateFriendRequestReqDTO;
import app.chatbox.dto.request.UpdateFriendRequestReqDTO;
import app.chatbox.dto.response.CreateFriendRequestResDTO;
import app.chatbox.dto.response.InboxUserResDTO;
import app.chatbox.dto.response.UpdateFriendRequestResDTO;
import app.chatbox.services.FriendRequestService;
import app.chatbox.services.InboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inbox")
@RequiredArgsConstructor
public class InboxController {
    private final InboxService service;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/messages")
    public ResponseEntity<InboxUserResDTO> getInboxMsgs(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(service.getUserInboxWithMsgs(user.getId(), id));
    }

}