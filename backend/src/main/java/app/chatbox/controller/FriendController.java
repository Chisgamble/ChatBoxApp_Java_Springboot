package app.chatbox.controller;

import app.chatbox.config.CustomUserDetails;
import app.chatbox.dto.FriendCardDTO;
import app.chatbox.dto.response.GeneralResDTO;
import app.chatbox.services.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friend")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @PreAuthorize("isAuthenticated() or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public List<FriendCardDTO> getAllFriendCards(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user){
        return friendService.getAllFriends(user.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFriend(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        friendService.deleteFriend(id, user.getId());
        return ResponseEntity.ok(
                new GeneralResDTO("Friend removed successfully")
        );
    }


}
