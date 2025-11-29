package app.chatbox.controller;

import app.chatbox.dto.response.UserResDTO;
import app.chatbox.model.AppUser;
import app.chatbox.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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
public class UserController {

    private final UserRepository repo;

    public UserController(UserRepository repo) {
        this.repo = repo;
    }

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

//    @PostMapping("/login")
//    public AppUser login(@RequestBody AppUser user) {
//        return repo.save(user);
//    }
//
//    @PostMapping("/register")
//    public int signup(@RequestBody AppUser user) {
//        if (user.getUsername().trim().isEmpty()){
//
//        }
//        return repo.save(user);
//    }

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body("Hello");
    }

}
