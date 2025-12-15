package com.example.services;

import com.example.api.InboxApi;
import com.example.dto.request.DeleteMsgsBySenderReq;
import com.example.dto.response.GeneralResDTO;
import com.example.dto.response.InboxUserResDTO;

import java.util.List;

public class InboxService {
    private final InboxApi api = new InboxApi();

    public InboxUserResDTO getInboxWithMessages (Long inboxId){
        return api.getInboxWithMsgs(inboxId);
    }

    public String deleteAllMessages(Long inboxId) {
        return api.deleteAllMessages(inboxId).message();
    }

    public String deleteMessagesBySender(Long inboxId, List<Long> inboxMsgIds){
        return api.deleteMessagesBySender(new DeleteMsgsBySenderReq(inboxMsgIds), inboxId).message();
    }
}
