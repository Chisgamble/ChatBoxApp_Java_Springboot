package app.chatbox.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserReqDTO(
    @Email @NotBlank(message = "Email cannot be empty")
    String email,

    @Size(min = 8, message = "Password must be at least 8 characters")
    String password
){ }