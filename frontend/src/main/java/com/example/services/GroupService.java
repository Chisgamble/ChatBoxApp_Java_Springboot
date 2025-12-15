package com.example.services;

import com.example.api.GroupApi;
import com.example.dto.GroupCardDTO;
import com.example.dto.GroupMemberDTO;
import com.example.dto.request.AddGroupMemberReqDTO;
import com.example.dto.request.CreateGroupReqDTO;
import com.example.dto.response.GroupUserResDTO;

import java.util.List;

public class GroupService {
    private final GroupApi api = new GroupApi();

    public GroupUserResDTO getInfoAndMsgs(Long groupId){
        return api.getInboxWithMsgs(groupId);
    }

    public String deleteAllMessages(Long groupId) {
        return api.deleteAllMessages(groupId).message();
    }

    public String leaveGroup (Long groupId) {
        return api.leaveGroup(groupId).message();
    }

    public String deleteGroup (Long groupId){
        return api.deleteGroup(groupId).message();
    }

    public GroupCardDTO createGroup (String groupName, List<Long> memberIds){
        CreateGroupReqDTO req = new CreateGroupReqDTO(groupName, memberIds);
        return api.createGroup(req);
    }

    public List<GroupMemberDTO> getAllMembers (Long groupId){
        return api.getAllMembers(groupId);
    }

    public String addMember (Long groupId, Long userId){
        return api.addMember(groupId, userId).message();
    }

    public String removeMember (Long groupId, Long userId){
        return api.removeMember(groupId, userId).message();
    }

    public GroupCardDTO changeGroupName(Long groupId, String name){
        return api.changeGroupName(groupId, name);
    }
}
