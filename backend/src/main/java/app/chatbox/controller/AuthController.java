package app.chatbox.controller;

import app.chatbox.dto.request.LoginReqDTO;
import app.chatbox.dto.request.RegisterReqDTO;
import app.chatbox.dto.response.LoginResDTO;
import app.chatbox.dto.response.RegisterResDTO;
import app.chatbox.dto.response.UserResDTO;
import app.chatbox.services.InboxService;
import app.chatbox.services.InboxMsgService;
import app.chatbox.mapper.UserMapper;
import app.chatbox.model.AppUser;
import app.chatbox.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserRepository userRepo;
    private final UserMapper userMapper;
    private final InboxService inboxService;
    private final PasswordEncoder passwordEncoder; // inject encoder

    @PostMapping("/register")
    public RegisterResDTO register(@RequestBody RegisterReqDTO req) {
        // Check if user already exists
        if (userRepo.existsByEmail(req.email())) {
            throw new RuntimeException("Email already registered");
        }

        // Create new user
        AppUser user = new AppUser();
        user.setUsername(req.username());
        user.setEmail(req.email());
        user.setPassword(passwordEncoder.encode(req.password())); // hash password
        user.setIsActive(true); // default active
        user.setRole("user");

        AppUser saved = userRepo.save(user);

        return  userMapper.toRegisterResDTO(saved);
    }

    @PostMapping("/login")
    public LoginResDTO login(@RequestBody LoginReqDTO req, HttpServletRequest http) {

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
        http.getSession(true); // Creates JSESSIONID

        AppUser user = userRepo.findByEmail(req.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new LoginResDTO(
                userMapper.toUserResDTO(user),
                inboxService.getInboxesWithLatestMsgs(user)
        );
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest req) {
        req.getSession().invalidate();
        return "Logged out";
    }
}

