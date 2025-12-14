package app.chatbox.dto.response;

import app.chatbox.dto.InboxMsgDTO;
import app.chatbox.dto.UserMiniDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InboxUserResDTO {
    private UserMiniDTO friend;
    private List<InboxMsgDTO> msgs;

    public InboxUserResDTO(UserMiniDTO friend, List<InboxMsgDTO> msgs){
        this.friend = friend;
        this.msgs = msgs;
    }
}
