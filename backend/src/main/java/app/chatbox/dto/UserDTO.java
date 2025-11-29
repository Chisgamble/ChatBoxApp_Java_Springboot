package app.chatbox.dto;

//setter, getter, equals, hashCode, toString
//import lombok.Data;
//
//@Data
public record UserDTO( 
     Long id,
     String username,
     String name,
     String email,
     Boolean is_active,
     Boolean is_locked,
     String role
){}
