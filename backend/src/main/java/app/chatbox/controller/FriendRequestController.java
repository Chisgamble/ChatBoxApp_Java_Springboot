package app.chatbox.controller;

import app.chatbox.dto.response.FriendRequestResDTO;
import app.chatbox.model.AppUser;
import app.chatbox.services.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/friend-requests")
@RequiredArgsConstructor
public class FriendRequestController {
    private final FriendRequestService service;

    @GetMapping("/incoming")
    public List<FriendRequestResDTO> getIncomingRequests(
            @AuthenticationPrincipal AppUser currentUser) {
        return service.getIncomingRequests(currentUser.getId());
    }
}
