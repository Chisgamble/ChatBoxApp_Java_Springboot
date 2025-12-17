package app.chatbox.controller;

import app.chatbox.config.CustomUserDetails;
import app.chatbox.dto.FriendCardDTO;
import app.chatbox.dto.NewUserListDTO;
import app.chatbox.dto.GroupCardDTO;
import app.chatbox.dto.MsgDTO;
import app.chatbox.dto.UserListDTO;
import app.chatbox.dto.YearlyGraphDTO;
import app.chatbox.dto.request.AdminCreateOrUpdateUserReqDTO;
import app.chatbox.dto.request.RegisterReqDTO;
import app.chatbox.dto.response.FriendRequestResDTO;
import app.chatbox.dto.response.GeneralResDTO;
import app.chatbox.dto.response.RegisterResDTO;
import app.chatbox.dto.response.StrangerCardResDTO;
import app.chatbox.services.*;
import app.chatbox.services.FriendRequestService;
import app.chatbox.services.FriendService;
import app.chatbox.services.UserService;
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

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<UserListDTO> getAllUsersAndData(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) List<String> username,
            @RequestParam(required = false) List<String> name,
            @RequestParam(required = false) String status
    ) {

        return ResponseEntity.ok(
                userService.getAllUsersAndData(sort, order, username, name, status)
        );
    }

    @PostMapping("/create")
    public ResponseEntity<StrangerCardResDTO> createAdminUser(@RequestBody AdminCreateOrUpdateUserReqDTO req) {
        return ResponseEntity.ok(userService.createUser(req));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<StrangerCardResDTO> updateUser(
            @PathVariable Long id,
            @RequestBody AdminCreateOrUpdateUserReqDTO req
    ) {
        return ResponseEntity.ok(userService.updateUserInfo(id, req));
    }

    @PreAuthorize("@authz.isCurrentUser(#id) or hasRole('ADMIN')")
    @PutMapping("/lock/{id}")
    public ResponseEntity<Map<String, String>> setLockStatus(
            @PathVariable Long id,
            @RequestParam boolean locked
    ) {
        userService.updateLockStatus(id, locked);

        // Return a real JSON object: {"success": "true"}
        return ResponseEntity.ok(Collections.singletonMap("status", "success"));
    }


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
    public GeneralResDTO delete(@PathVariable Long id) {
        userService.delete(id);
        return new GeneralResDTO("Delete user" + id);
    }

    @PreAuthorize("@authz.isCurrentUser(#id) or hasRole('ADMIN')")
    @GetMapping("/{id}/friends")
    public ResponseEntity<List<FriendCardDTO>> getAllFriendCards(@PathVariable Long id) {
        return ResponseEntity.ok(friendService.getAllUserFriends(id));
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

    @GetMapping("/new-list")
    public ResponseEntity<?> getNewUserList(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String order
    ) {
        try {
            NewUserListDTO result = userService.getNewUsers(username, email, startDate, endDate, order);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            // Returns 400 Bad Request if dates are invalid
            return ResponseEntity.badRequest().body(new GeneralResDTO(e.getMessage()));
        }
    }

    @GetMapping("/new-graph")
    public ResponseEntity<YearlyGraphDTO> getNewUsersGraph(@RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(userService.getNewUserGraph(year));
    }
}
