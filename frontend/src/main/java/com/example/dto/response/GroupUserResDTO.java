package com.example.dto.response;

import com.example.dto.GroupMemberDTO;
import com.example.dto.GroupMsgDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GroupUserResDTO {
    private GroupMemberDTO userInGroup;
    private List<GroupMsgDTO> msgs;

    public GroupUserResDTO(GroupMemberDTO userInGroup, List<GroupMsgDTO> msgs){
        this.userInGroup = userInGroup;
        this.msgs = msgs;
    }
}