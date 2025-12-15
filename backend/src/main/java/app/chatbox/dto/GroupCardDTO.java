package app.chatbox.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupCardDTO {
    private Long id;
    private Long senderId;
    private String groupname;
    private String last_msg;

    public GroupCardDTO(Long id, Long senderId, String groupname, String last_msg){
        this.id = id;
        this.senderId = senderId;
        this.groupname = groupname;
        this.last_msg = last_msg;
    }
}
