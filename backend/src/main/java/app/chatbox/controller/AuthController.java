package app.chatbox.controller;

import app.chatbox.config.CustomUserDetails;
import app.chatbox.dto.request.ChangePasswordReqDTO;
import app.chatbox.dto.request.LoginReqDTO;
import app.chatbox.dto.request.RegisterReqDTO;
import app.chatbox.dto.request.ResetPasswordReqDTO;
import app.chatbox.dto.response.GeneralResDTO;
import app.chatbox.dto.response.LoginResDTO;
import app.chatbox.dto.response.LogoutResDTO;
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

import java.util.Map;

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
    public LogoutResDTO logout(HttpServletRequest req) {
        req.getSession().invalidate();
        return new LogoutResDTO("Logged out");
    }

    @PostMapping("/reset-password")
    public GeneralResDTO resetPassword(@RequestBody ResetPasswordReqDTO req) {
        userService.resetPasswordAndSendEmail(req.email());
        return new GeneralResDTO("A new password has been sent to your email.");
    }

    @PostMapping("/change-password")
    public GeneralResDTO changePassword(
            @RequestBody ChangePasswordReqDTO req,
            Authentication auth // lấy user hiện tại
    ) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        if (!passwordEncoder.matches(req.oldPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid old password");
        }

        userService.updatePassword(user.getId(), req.newPassword());

        return new GeneralResDTO("Password updated successfully");
    }
}

