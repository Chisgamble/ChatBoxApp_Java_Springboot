package com.example.services;

import com.example.api.GroupApi;
import com.example.dto.response.GroupUserResDTO;

public class GroupService {
    private final GroupApi api = new GroupApi();

    public GroupUserResDTO getInfoAndMsgs(Long groupId){
        return api.getInboxWithMsgs(groupId);
    }
}
