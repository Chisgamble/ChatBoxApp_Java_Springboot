package com.example.services;

import com.example.api.GroupApi;
import com.example.dto.AddMemberCardDTO;
import com.example.dto.GroupCardDTO;
import com.example.dto.GroupMemberDTO;
import com.example.dto.request.AddGroupMemberReqDTO;
import com.example.dto.request.AddMembersReqDTO;
import com.example.dto.request.CreateGroupReqDTO;
import com.example.dto.request.DeleteMsgsBySenderReq;
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

    public String deleteMessagesBySender (Long groupId, List<Long> msgIds){
        DeleteMsgsBySenderReq req = new DeleteMsgsBySenderReq(msgIds);
        return api.deleteMessagesBySender(groupId, req).message();
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

    public List<AddMemberCardDTO> getAllFriendsNotInGroup (Long groupId, Long currentUserId){
        return api.getAllFriendsNotInGroup(groupId, currentUserId);
    }

    public String addMember (Long groupId, Long userId){
        return api.addMember(groupId, userId).message();
    }

    public String addMembers (Long groupId, List<Long> userIds, Long currentUserId){
        AddMembersReqDTO req = new AddMembersReqDTO(groupId, userIds);
        return api.addMembers(groupId, req, currentUserId).message();
    }

    public String removeMember (Long groupId, Long userId){
        return api.removeMember(groupId, userId).message();
    }

    public String promoteMember (Long groupId, Long targetUserId){
        return api.promoteMember(groupId, targetUserId).message();
    }

    public GroupCardDTO changeGroupName(Long groupId, String name){
        return api.changeGroupName(groupId, name);
    }
}
