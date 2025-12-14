package com.example.services;

import com.example.api.InboxApi;
import com.example.dto.response.InboxUserResDTO;

public class InboxService {
    private final InboxApi api = new InboxApi();

    public InboxUserResDTO getInboxWithMessages (Long inboxId){
        return api.getInboxWithMsgs(inboxId);
    }
}
