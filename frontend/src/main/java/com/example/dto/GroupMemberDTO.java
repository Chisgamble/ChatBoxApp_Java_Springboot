package com.example.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GroupMemberDTO {
    private Long id;
    private Long userId;
    private Long groupId;
    private String email;
    private String username;
    private String role;

    public GroupMemberDTO(Long id, Long userId, Long groupId, String email, String username, String role){
        this.id = id;
        this.userId = userId;
        this.groupId = groupId;
        this.email = email;
        this.username = username;
        this.role = role;
    }
}
