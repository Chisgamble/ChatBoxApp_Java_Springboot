package com.example.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddGroupMemberReqDTO {
    private Long userId;

    public AddGroupMemberReqDTO(Long userId){
        this.userId = userId;
    }
}
