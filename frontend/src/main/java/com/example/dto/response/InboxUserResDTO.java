package com.example.dto.response;


import com.example.dto.InboxMsgDTO;
import com.example.dto.UserMiniDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class InboxUserResDTO {
    private UserMiniDTO friend;
    private List<InboxMsgDTO> msgs;

    public InboxUserResDTO(UserMiniDTO friend, List<InboxMsgDTO> msgs){
        this.friend = friend;
        this.msgs = msgs;
    }
}
