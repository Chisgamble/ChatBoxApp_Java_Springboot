package app.chatbox.controller;

import app.chatbox.dto.request.LoginReqDTO;
import app.chatbox.dto.request.RegisterReqDTO;
import app.chatbox.dto.response.LoginResDTO;
import app.chatbox.dto.response.RegisterResDTO;
import app.chatbox.mapper.UserMapper;


import app.chatbox.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder; // inject encoder

    @PostMapping("/register")
    public RegisterResDTO register(@RequestBody RegisterReqDTO req) {
        return userService.register(req);
    }

    @PostMapping("/login")
    public LoginResDTO login(@RequestBody LoginReqDTO req, HttpServletRequest http, HttpServletResponse res) {

        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(auth);
        http.getSession(true); // Creates JSESSIONID
        new HttpSessionSecurityContextRepository().saveContext(securityContext, http, res);

        return userService.login(req);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest req) {
        req.getSession().invalidate();
        return "Logged out";
    }
}

