package app.chatbox.config;

import app.chatbox.dto.response.GeneralResDTO;
import app.chatbox.dto.response.LoginResDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Wrong login credentials
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<LoginResDTO> handleBadCredentials(BadCredentialsException ex) {
        LoginResDTO response = new LoginResDTO(null, "Invalid email or password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // Validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GeneralResDTO> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.badRequest().body(new GeneralResDTO(message));
    }

    //Custom business errors thrown manually
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<GeneralResDTO> handleRuntime(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new GeneralResDTO(ex.getMessage()));
    }

    // Unknown / unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralResDTO> handleGeneral(Exception ex) {
        ex.printStackTrace(); // log server
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new GeneralResDTO("Internal server error"));
    }
}
