package com.example.dto;

import java.time.LocalDate;

//setter, getter, equals, hashCode, toString
//import lombok.Data;
//
//@Data
public record UserDTO( 
     Long id,
     String username,
     String name,
     String gender,
     String email,
     String address,
     String dob,
     Boolean is_active,
     Boolean is_locked,
     String role,
     Boolean is_banned
){}
