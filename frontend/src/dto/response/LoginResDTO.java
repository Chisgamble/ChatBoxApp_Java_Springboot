package dto.response;

import dto.InboxDTO;

import java.util.List;

public record LoginResDTO(
    Long id,
    String email,
    String initials,
    String role
) {}
