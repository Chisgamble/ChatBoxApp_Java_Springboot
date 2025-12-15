package app.chatbox.controller;

import app.chatbox.dto.request.CreateFriendRequestReqDTO;
import app.chatbox.dto.request.UpdateFriendRequestReqDTO;
import app.chatbox.dto.response.CreateFriendRequestResDTO;
import app.chatbox.dto.response.UpdateFriendRequestResDTO;
import app.chatbox.service.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friend-requests")
@RequiredArgsConstructor
public class FriendRequestController {
    private final FriendRequestService service;

    @PreAuthorize("@authz.currentUserId() == #req.userId or hasRole('ADMIN')")
    @PostMapping("/{id}")
    public ResponseEntity<UpdateFriendRequestResDTO> updateFriendRequest(
            @RequestBody UpdateFriendRequestReqDTO req,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.updateFriendRequest(req, id));
    }

    @PreAuthorize("@authz.isCurrentUser(#req.senderId) or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CreateFriendRequestResDTO> createFriendRequest(
            @RequestBody CreateFriendRequestReqDTO req
    ) {
        return ResponseEntity.ok(service.createFriendRequest(req));
    }


}
