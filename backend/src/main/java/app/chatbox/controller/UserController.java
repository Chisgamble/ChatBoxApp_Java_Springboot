package app.chatbox.controller;

import app.chatbox.dto.FriendCardDTO;
import app.chatbox.dto.NewUserListDTO;
import app.chatbox.dto.UserListDTO;
import app.chatbox.dto.YearlyGraphDTO;
import app.chatbox.dto.request.AdminCreateOrUpdateUserReqDTO;
import app.chatbox.dto.request.RegisterReqDTO;
import app.chatbox.dto.response.FriendRequestResDTO;
import app.chatbox.dto.response.GeneralResDTO;
import app.chatbox.dto.response.RegisterResDTO;
import app.chatbox.dto.response.StrangerCardResDTO;
import app.chatbox.service.FriendRequestService;
import app.chatbox.service.FriendService;
import app.chatbox.service.UserService;
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

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final FriendService friendService;
    private final FriendRequestService friendRequestService;
    private final UserService userService;

//    @GetMapping("/")
//    public List<StrangerCardResDTO> getAllUsers() {
//        return userService.getAllStrangerCards();
//    }
    @PreAuthorize("@authz.isCurrentUser(#id)")
    @GetMapping("/{id}/strangers")
    public ResponseEntity<List<StrangerCardResDTO>> getAllStrangers(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getAllStrangerCards(id));
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
    public ResponseEntity<StrangerCardResDTO> createAdminUser(@RequestBody AdminCreateOrUpdateUserReqDTO req) {
        return ResponseEntity.ok(userService.createUser(req));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<StrangerCardResDTO> updateUser(
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
    @GetMapping("/{id}/friend-requests")
    public ResponseEntity<List<FriendRequestResDTO>> getIncomingRequests(@PathVariable Long id) {
        return ResponseEntity.ok(friendRequestService.getIncomingRequests(id));
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
