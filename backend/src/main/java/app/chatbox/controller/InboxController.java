package app.chatbox.controller;

import app.chatbox.config.CustomUserDetails;
import app.chatbox.dto.InboxMsgDTO;
import app.chatbox.dto.request.CreateFriendRequestReqDTO;
import app.chatbox.dto.request.DeleteMsgsBySenderReq;
import app.chatbox.dto.request.UpdateFriendRequestReqDTO;
import app.chatbox.dto.response.CreateFriendRequestResDTO;
import app.chatbox.dto.response.GeneralResDTO;
import app.chatbox.dto.response.InboxUserResDTO;
import app.chatbox.dto.response.UpdateFriendRequestResDTO;
import app.chatbox.services.FriendRequestService;
import app.chatbox.services.InboxMsgService;
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
    private final InboxMsgService inboxMsgService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/messages")
    public ResponseEntity<InboxUserResDTO> getInboxMsgs(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(service.getUserInboxWithMsgs(user.getId(), id));
    }

    @PreAuthorize("isAuthenticated() or hasRole('ADMIN')")
    @DeleteMapping("/{id}/messages/all")
    public ResponseEntity<GeneralResDTO> deleteAllMessages(@PathVariable Long id) {
        service.deleteAllMessages(id);
        return ResponseEntity.ok(
                new GeneralResDTO("All inbox messages removed successfully"));
    }

    @PreAuthorize("isAuthenticated() or hasRole('ADMIN')")
    @DeleteMapping("/{id}/messages")
    public ResponseEntity<GeneralResDTO> deleteMessagesBySender(
            @PathVariable Long id,
            @RequestBody DeleteMsgsBySenderReq req,
            @AuthenticationPrincipal CustomUserDetails user) {
        inboxMsgService.deleteMessagesBySenderId(id, req, user.getId());
        return ResponseEntity.ok(
                new GeneralResDTO("Messages removed successfully"));
    }

}