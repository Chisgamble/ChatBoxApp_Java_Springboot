package app.chatbox.controller;

import app.chatbox.dto.FriendCardDTO;
import app.chatbox.dto.response.FriendRequestResDTO;
import app.chatbox.dto.response.UserResDTO;
import app.chatbox.model.AppUser;
import app.chatbox.services.FriendRequestService;
import app.chatbox.services.FriendService;
import app.chatbox.services.UserService;
import jakarta.validation.Valid;
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

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository repo;
    private final FriendService friendService;
    private final FriendRequestService friendRequestService;

    @GetMapping("/getall")
    public List<AppUser> getAllUsers() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public AppUser getUser(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

//    @PostMapping
//    public UserResDTO addUser(@RequestBody @Valid UserRequest request) {
//        return UserService.addUser(request);
//    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        repo.deleteById(id);
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
