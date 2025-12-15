package app.chatbox.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateGroupReqDTO {
    private String groupName;
    private List<Long> memberIds;

    public CreateGroupReqDTO(String groupName, List<Long> memberIds){
        this.groupName = groupName;
        this.memberIds = memberIds;
    }
}
