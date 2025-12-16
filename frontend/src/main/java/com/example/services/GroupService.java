package com.example.services;

import com.example.api.GroupApi;
import com.example.dto.GroupCardDTO;
import com.example.dto.GroupDataDTO;
import com.example.dto.GroupListDataDTO;
import com.example.dto.GroupMemberDTO;
import com.example.dto.request.AddGroupMemberReqDTO;
import com.example.dto.request.CreateGroupReqDTO;
import com.example.dto.response.GroupUserResDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class GroupService {
    private final GroupApi api = new GroupApi();
    private final DateTimeFormatter apiFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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


    public List<GroupDataDTO> getGroupListData(
            List<String> nameFilters,
            String startDate,
            String endDate,
            String sortBy,
            String sortDir
    ) {
        // 1. Prepare Parameters (Business Logic)
        // We use only the first element if multiple name filters are present
        String nameQuery = nameFilters.isEmpty() ? null : nameFilters.get(0);

        String startStr = (startDate != null) ? startDate : null;
        String endStr = (endDate != null) ? endDate : null;

        try {
            // 2. Call API Client
            GroupListDataDTO wrapper = api.getAllGroupData(
                    nameQuery,
                    startStr,
                    endStr,
                    sortBy,
                    sortDir
            );

            // 3. Process Response
            return wrapper.groups();

        } catch (RuntimeException e) {
            e.printStackTrace();
            // Handle API failure, return empty list
            return Collections.emptyList();
        }
    }
}
