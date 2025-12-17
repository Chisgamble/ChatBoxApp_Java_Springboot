package com.example.api;

import com.example.dto.AddMemberCardDTO;
import com.example.dto.FriendCardDTO;
import com.example.dto.GroupCardDTO;
import com.example.dto.GroupListDataDTO;
import com.example.dto.GroupMemberDTO;
import com.example.dto.request.*;
import com.example.dto.response.GeneralResDTO;
import com.example.dto.response.GroupUserResDTO;
import com.example.util.HttpClientUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GroupApi {
    private static final String BASE_URL = "http://localhost:8080/api/group";

    public GroupUserResDTO getInboxWithMsgs(Long inboxId) {
        String url = BASE_URL + "/" +  inboxId.toString() + "/messages" ;
        GroupUserResDTO response = HttpClientUtil.get(url, GroupUserResDTO.class);
        return response;
    }

    public GeneralResDTO deleteAllMessages(Long groupId) {
        String url = BASE_URL + "/" + groupId.toString() + "/messages/all";
        return HttpClientUtil.deleteJson(url, null, GeneralResDTO.class);
    }

    public GeneralResDTO deleteMessagesBySender(Long groupId, DeleteMsgsBySenderReq req){
        String url = BASE_URL + "/" + groupId + "/messages";
        return HttpClientUtil.deleteJson(url, req, GeneralResDTO.class);
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

    public List<AddMemberCardDTO> getAllFriendsNotInGroup (Long groupId, Long currentUserId){
        String url = BASE_URL + "/" + groupId + "/available-friends";
        return HttpClientUtil.get(url,  new TypeReference<List<AddMemberCardDTO>>() {});
    }

    public GeneralResDTO addMember(Long groupId, Long userId) {
        return HttpClientUtil.postJson(
                BASE_URL + "/" + groupId + "/member",
                new AddGroupMemberReqDTO(userId),
                GeneralResDTO.class
        );
    }

    public GeneralResDTO addMembers(Long groupId, AddMembersReqDTO req, Long currentUserId){
        return HttpClientUtil.postJson(
                BASE_URL + "/" + groupId + "/members",
                req,
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

    public GeneralResDTO promoteMember(Long groupId, Long targetUserId){
        return HttpClientUtil.putJson(
                BASE_URL + "/" + groupId + "/members/" + targetUserId + "/promote",
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

    public GroupListDataDTO getAllGroupData(String nameFilter, String startDate, String endDate, String sortBy, String sortDir) {
        StringBuilder urlBuilder = new StringBuilder(BASE_URL + "/getall/data?");

        try {
            if (nameFilter != null && !nameFilter.isEmpty()) {
                String encodedName = URLEncoder.encode(nameFilter, StandardCharsets.UTF_8.toString());
                urlBuilder.append("nameFilter=").append(encodedName).append("&");
            }

            if (startDate != null) {
                // FIX: Encode the date string to handle spaces and colons
                String encodedStartDate = URLEncoder.encode(startDate, StandardCharsets.UTF_8.toString());
                urlBuilder.append("startDate=").append(encodedStartDate).append("&");
            }

            if (endDate != null) {
                // FIX: Encode the date string
                String encodedEndDate = URLEncoder.encode(endDate, StandardCharsets.UTF_8.toString());
                urlBuilder.append("endDate=").append(encodedEndDate).append("&");
            }

            if (sortBy != null) {
                String encodedSortBy = URLEncoder.encode(sortBy, StandardCharsets.UTF_8.toString());
                urlBuilder.append("sortBy=").append(encodedSortBy).append("&");
            }
            if (sortDir != null) {
                String encodedSortDir = URLEncoder.encode(sortDir, StandardCharsets.UTF_8.toString());
                urlBuilder.append("sortDir=").append(encodedSortDir).append("&");
            }

        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("URL Encoding failed", e);
        }

        String finalUrl = urlBuilder.toString();
        if (finalUrl.endsWith("&")) {
            finalUrl = finalUrl.substring(0, finalUrl.length() - 1);
        }

        return HttpClientUtil.get(finalUrl, GroupListDataDTO.class);
    }

}
