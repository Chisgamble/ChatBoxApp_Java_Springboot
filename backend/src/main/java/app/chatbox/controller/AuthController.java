package app.chatbox.controller;

import app.chatbox.config.CustomUserDetails;
import app.chatbox.dto.UserMiniDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
    public ResponseEntity<LoginResDTO> login(@RequestBody LoginReqDTO req, HttpServletRequest http, HttpServletResponse res) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.email(), req.password())
            );

            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(auth);
            http.getSession(true); // Creates JSESSIONID
            new HttpSessionSecurityContextRepository().saveContext(securityContext, http, res);

            UserMiniDTO user = userService.login(req);
            LoginResDTO loginRes = new LoginResDTO(user, "Success");
            return ResponseEntity.ok(loginRes);
        } catch (BadCredentialsException ex) {
            // 401 Unauthorized for invalid credentials
            LoginResDTO loginRes = new LoginResDTO(null,  "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginRes);
        } catch (Exception e) {
            // 500 Internal Server Error for any other issue
            LoginResDTO loginRes = new LoginResDTO(null,
                    "Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(loginRes);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<LogoutResDTO> logout(HttpServletRequest req) {
        req.getSession().invalidate();
        return ResponseEntity.ok(new LogoutResDTO("Logged out"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<GeneralResDTO> resetPassword(@RequestBody ResetPasswordReqDTO req) {
        userService.resetPasswordAndSendEmail(req.email());
        return ResponseEntity.ok(new GeneralResDTO("A new password has been sent to your email."));
    }

    @PostMapping("/change-password")
    public ResponseEntity<GeneralResDTO> changePassword(
            @RequestBody ChangePasswordReqDTO req,
            Authentication auth
    ) {
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();

        if (!passwordEncoder.matches(req.oldPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid old password");
        }

        userService.updatePassword(user.getId(), req.newPassword());
        return ResponseEntity.ok(new GeneralResDTO("Password updated successfully"));
    }
}

