package app.chatbox.controller;

import app.chatbox.config.CustomUserDetails;
import app.chatbox.dto.FriendCardDTO;
import app.chatbox.dto.GroupCardDTO;
import app.chatbox.dto.MsgDTO;
import app.chatbox.dto.UserListDTO;
import app.chatbox.dto.request.RegisterReqDTO;
import app.chatbox.dto.response.FriendRequestResDTO;
import app.chatbox.dto.response.GeneralResDTO;
import app.chatbox.dto.response.RegisterResDTO;
import app.chatbox.dto.response.StrangerCardResDTO;
import app.chatbox.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

import app.chatbox.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final FriendService friendService;
    private final FriendRequestService friendRequestService;
    private final UserService userService;
    private final GroupService groupService;
    private final UserBlockService userBlockService;

//    @GetMapping("/")
//    public List<StrangerCardResDTO> getAllUsers() {
//        return userService.getAllStrangerCards();
//    }
    @PreAuthorize("@authz.isCurrentUser(#id)")
    @GetMapping("/{id}/strangers")
    public ResponseEntity<List<StrangerCardResDTO>> getAllStrangers(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getAllStrangerCards(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getall/data")
    public UserListDTO getAllUsersAndData() {return userService.getAllUsersAndData();}

    @GetMapping("/{id}")
    public StrangerCardResDTO getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping("/register")
    public RegisterResDTO register(@RequestBody RegisterReqDTO req) {
        return userService.register(req);
    }

//    @PostMapping
//    public StrangerCardResDTO addUser(@RequestBody @Valid UserRequest request) {
//        return UserService.addUser(request);
//    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @PreAuthorize("@authz.isCurrentUser(#id) or hasRole('ADMIN')")
    @GetMapping("/{id}/friends")
    public ResponseEntity<List<FriendCardDTO>> getAllFriendCards(@PathVariable Long id) {
        return ResponseEntity.ok(friendService.getAllFriends(id));
    }

    @PreAuthorize("@authz.isCurrentUser(#id) or hasRole('ADMIN')")
    @GetMapping("/{id}/groups")
    public ResponseEntity<List<GroupCardDTO>> getAllGroupCards(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getAllGroups(id));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/messages/me")
    public ResponseEntity<List<MsgDTO>> getMyMessages(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(
                userService.findAllMsgsByUser(user.getId())
        );
    }


    @PreAuthorize("@authz.isCurrentUser(#id) or hasRole('ADMIN')")
    @GetMapping("/{id}/friend-requests")
    public ResponseEntity<List<FriendRequestResDTO>> getIncomingRequests(@PathVariable Long id) {
        return ResponseEntity.ok(friendRequestService.getIncomingRequests(id));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/block")
    public ResponseEntity<GeneralResDTO> blockUser(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        userBlockService.blockUser(user.getId(), id);
        return ResponseEntity.ok(
                new GeneralResDTO("User blocked successfully")
        );
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/report-spam")
    public ResponseEntity<GeneralResDTO> reportSpam(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        userService.reportSpam(id, user.getId());
        return ResponseEntity.ok(new GeneralResDTO("Reported successfully"));
    }

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body("Hello");
    }

}
