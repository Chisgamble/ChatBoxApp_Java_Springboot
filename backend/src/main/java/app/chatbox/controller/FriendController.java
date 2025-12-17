package app.chatbox.controller;

import app.chatbox.config.CustomUserDetails;
import app.chatbox.dto.FriendCardDTO;
import app.chatbox.dto.FriendListDataDTO;
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
        return friendService.getAllUserFriends(user.getId());
    }

    @PreAuthorize("isAuthenticated() or hasRole('ADMIN')")
    @GetMapping("/by/{id}")
    public List<FriendCardDTO> getAllFriendCardsById(@PathVariable Long id){
        return friendService.getAllFriends(id);
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



    @PreAuthorize("isAuthenticated() or hasRole('ADMIN')")
    @GetMapping("/getall/data")
    public ResponseEntity<List<FriendListDataDTO>> getFriendListData(
            @RequestParam(required = false) String username,
            @RequestParam(required = false, defaultValue = "username") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String fcSymbol,
            @RequestParam(required = false) Integer fcVal
    ) {
        return ResponseEntity.ok(
                friendService.getFriendListData(username, sortBy, sortDir, fcSymbol, fcVal)
        );
    }
}
