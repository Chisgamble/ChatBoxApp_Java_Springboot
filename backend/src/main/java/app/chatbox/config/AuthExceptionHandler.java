package app.chatbox.config;

import app.chatbox.dto.response.LoginResDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<LoginResDTO> handleBadCredentials(BadCredentialsException ex) {
        // Return 401 + a meaningful message
        LoginResDTO response = new LoginResDTO(
                null,
                null,
                null,
                "Invalid email or password"
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

}
