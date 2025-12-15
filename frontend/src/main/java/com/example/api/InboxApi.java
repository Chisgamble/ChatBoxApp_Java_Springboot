package com.example.api;

import com.example.dto.request.DeleteMsgsBySenderReq;
import com.example.dto.response.GeneralResDTO;
import com.example.dto.response.InboxUserResDTO;
import com.example.util.HttpClientUtil;

public class InboxApi {
    private static final String BASE_URL = "http://localhost:8080/api/inbox";

    public InboxUserResDTO getInboxWithMsgs(Long inboxId) {
        String url = BASE_URL + "/" +  inboxId.toString() + "/messages" ;
        InboxUserResDTO response = HttpClientUtil.get(url, InboxUserResDTO.class);
        return response;
    }

    public GeneralResDTO deleteAllMessages(Long inboxId) {
        String url = BASE_URL + "/" + inboxId.toString() + "/messages/all";
        return HttpClientUtil.deleteJson(url, null, GeneralResDTO.class);
    }

    public GeneralResDTO deleteMessagesBySender(DeleteMsgsBySenderReq req, Long inboxId){
        String url = BASE_URL + "/" + inboxId + "/messages";
        return HttpClientUtil.deleteJson(url, req, GeneralResDTO.class);
    }
}