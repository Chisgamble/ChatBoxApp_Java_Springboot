package dto.request;

public record RegisterReqDTO(
    String username,
    String email,
    String password
){}
