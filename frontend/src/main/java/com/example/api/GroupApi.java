package com.example.api;

import com.example.dto.response.GroupUserResDTO;
import com.example.util.HttpClientUtil;

public class GroupApi {
    private static final String BASE_URL = "http://localhost:8080/api/group";

    public GroupUserResDTO getInboxWithMsgs(Long inboxId) {
        String url = BASE_URL + "/" +  inboxId.toString() + "/messages" ;
        GroupUserResDTO response = HttpClientUtil.get(url, GroupUserResDTO.class);
        return response;
    }
}
