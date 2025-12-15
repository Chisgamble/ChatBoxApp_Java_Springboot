package app.chatbox.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class DeleteMsgsBySenderReq {
    List<Long> msgIds;
}
