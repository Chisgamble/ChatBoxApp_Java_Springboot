package com.example.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GroupCardDTO {
    private Long id;
    private Long senderId;
    private String groupname;
    private String last_msg;

    public GroupCardDTO(long id, long senderId, String groupname, String last_msg){
        this.id = id;
        this.senderId = senderId;
        this.groupname = groupname;
        this.last_msg = last_msg;
    }
}
