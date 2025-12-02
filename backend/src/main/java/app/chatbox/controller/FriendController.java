package app.chatbox.controller;

import app.chatbox.dto.FriendCardDTO;
import app.chatbox.dto.FriendCardListDTO;
import app.chatbox.services.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/friend")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public FriendCardListDTO getAllFriendCards(@PathVariable Long id){
        return friendService.getAllFriends(id);
    }
}
