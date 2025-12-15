package com.example.api;

import com.example.dto.FriendCardDTO;
import com.example.dto.GroupCardDTO;
import com.example.dto.GroupMemberDTO;
import com.example.dto.request.AddGroupMemberReqDTO;
import com.example.dto.request.ChangeGroupNameReqDTO;
import com.example.dto.request.CreateGroupReqDTO;
import com.example.dto.response.GeneralResDTO;
import com.example.dto.response.GroupUserResDTO;
import com.example.util.HttpClientUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class GroupApi {
    private static final String BASE_URL = "http://localhost:8080/api/group";

    public GroupUserResDTO getInboxWithMsgs(Long inboxId) {
        String url = BASE_URL + "/" +  inboxId.toString() + "/messages" ;
        GroupUserResDTO response = HttpClientUtil.get(url, GroupUserResDTO.class);
        return response;
    }

    public GeneralResDTO deleteAllMessages(Long groupId) {
        String url = BASE_URL + "/" + groupId.toString() + "/messages";
        return HttpClientUtil.deleteJson(url, null, GeneralResDTO.class);
    }

    public GeneralResDTO leaveGroup(Long groupId) {
        String url = BASE_URL + "/" + groupId.toString() + "/leave";
        GeneralResDTO res = HttpClientUtil.postJson(url, null, GeneralResDTO.class);
        return res;
    }

    public GeneralResDTO deleteGroup(Long groupId) {
        String url = BASE_URL + "/" + groupId.toString();
        GeneralResDTO res = HttpClientUtil.deleteJson(url, null, GeneralResDTO.class);
        return res;
    }

    public GroupCardDTO createGroup(CreateGroupReqDTO req) {
        return HttpClientUtil.postJson(BASE_URL, req, GroupCardDTO.class);
    }

    public List<GroupMemberDTO> getAllMembers (Long groupId){
        String url = BASE_URL + "/" + groupId + "/members";
        return HttpClientUtil.get(url,  new TypeReference<List<GroupMemberDTO>>() {});
    }

    public GeneralResDTO addMember(Long groupId, Long userId) {
        return HttpClientUtil.postJson(
                BASE_URL + "/" + groupId + "/members",
                new AddGroupMemberReqDTO(userId),
                GeneralResDTO.class
        );
    }

    public GeneralResDTO removeMember(Long groupId, Long userId) {
        return HttpClientUtil.deleteJson(
                BASE_URL + "/" + groupId + "/members/" + userId,
                null,
                GeneralResDTO.class
        );
    }

    public GroupCardDTO changeGroupName(Long groupId, String name) {
        return HttpClientUtil.putJson(
                BASE_URL + "/" + groupId + "/name",
                new ChangeGroupNameReqDTO(name),
                GroupCardDTO.class
        );
    }

}
