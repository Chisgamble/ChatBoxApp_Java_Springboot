package dto;

public record UserMiniDTO(
        long id,
        String username,
        String initials,
        Boolean isActive
) {}
