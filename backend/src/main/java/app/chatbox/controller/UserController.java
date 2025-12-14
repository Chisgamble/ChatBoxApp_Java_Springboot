package app.chatbox.controller;

import app.chatbox.dto.FriendCardDTO;
import app.chatbox.dto.UserListDTO;
import app.chatbox.dto.request.AdminCreateOrUpdateUserReqDTO;
import app.chatbox.dto.request.RegisterReqDTO;
import app.chatbox.dto.response.FriendRequestResDTO;
import app.chatbox.dto.response.RegisterResDTO;
import app.chatbox.dto.response.UserResDTO;
import app.chatbox.services.FriendRequestService;
import app.chatbox.services.FriendService;
import app.chatbox.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

import app.chatbox.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository repo;
    private final FriendService friendService;
    private final FriendRequestService friendRequestService;
    private final UserService userService;

    @GetMapping("/getall")
    public List<UserResDTO> getAllUsers() {
        return userService.getAllUsers();
    }

//    @PreAuthorize("hasRole('ADMIN')")
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
    public ResponseEntity<UserResDTO> createAdminUser(@RequestBody AdminCreateOrUpdateUserReqDTO req) {
        return ResponseEntity.ok(userService.createUser(req));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserResDTO> updateUser(
            @PathVariable Long id,
            @RequestBody AdminCreateOrUpdateUserReqDTO req
    ) {
        return ResponseEntity.ok(userService.updateUserInfo(id, req));
    }

    @PutMapping("/lock/{id}")
    public ResponseEntity<Void> setLockStatus(
            @PathVariable Long id,
            @RequestParam boolean locked
    ) {
        userService.updateLockStatus(id, locked);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{id}")
    public UserResDTO getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping("/register")
    public RegisterResDTO register(@RequestBody RegisterReqDTO req) {
        return userService.register(req);
    }

//    @PostMapping
//    public UserResDTO addUser(@RequestBody @Valid UserRequest request) {
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
    @GetMapping("/{id}/friend-requests")
    public ResponseEntity<List<FriendRequestResDTO>> getIncomingRequests(@PathVariable Long id) {
        return ResponseEntity.ok(friendRequestService.getIncomingRequests(id));
    }

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body("Hello");
    }

}
