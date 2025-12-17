package app.chatbox.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddMembersReqDTO {
    private Long groupId;
    private List<Long> userIds;
}
