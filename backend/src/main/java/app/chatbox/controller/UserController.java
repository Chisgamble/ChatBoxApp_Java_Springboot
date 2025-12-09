package app.chatbox.controller;

import app.chatbox.dto.UserDTO;
import app.chatbox.dto.UserListDTO;
import app.chatbox.dto.request.LoginReqDTO;
import app.chatbox.dto.request.RegisterReqDTO;
import app.chatbox.dto.response.LoginResDTO;
import app.chatbox.dto.response.RegisterResDTO;
import app.chatbox.dto.response.UserResDTO;
import app.chatbox.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/getall")
    public List<UserResDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/getall/data")
    public UserListDTO getAllUsersAndData() {return userService.getAllUsersAndData();}

    @GetMapping("/{id}")
    public UserResDTO getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping("/register")
    public RegisterResDTO register(@RequestBody RegisterReqDTO req) {
        return userService.register(req);
    }

    @PostMapping("/login")
    public LoginResDTO login(@RequestBody LoginReqDTO req) {
        return userService.login(req);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
