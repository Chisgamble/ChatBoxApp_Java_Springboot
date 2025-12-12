package app.chatbox.dto;

import java.time.LocalDate;

//setter, getter, equals, hashCode, toString
//import lombok.Data;
//
//@Data
public record UserDTO( 
     Long id,
     String username,
     String name,
     String email,
     String gender,
     String address,
     LocalDate dob,
     Boolean is_banned,
     Boolean is_active,
     Boolean is_locked,
     String role
){}
