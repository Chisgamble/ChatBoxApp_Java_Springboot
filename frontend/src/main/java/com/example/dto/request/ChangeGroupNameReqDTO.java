package com.example.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangeGroupNameReqDTO {
    private String newName;

    public ChangeGroupNameReqDTO(String newName) {
        this.newName = newName;
    }
}
