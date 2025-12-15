package app.chatbox.dto.response;

import app.chatbox.dto.GroupMemberDTO;
import app.chatbox.dto.GroupMsgDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupUserResDTO {
    private GroupMemberDTO userInGroup;
    private List<GroupMsgDTO> msgs;

    public GroupUserResDTO(GroupMemberDTO userInGroup, List<GroupMsgDTO> msgs){
        this.userInGroup = userInGroup;
        this.msgs = msgs;
    }
}
