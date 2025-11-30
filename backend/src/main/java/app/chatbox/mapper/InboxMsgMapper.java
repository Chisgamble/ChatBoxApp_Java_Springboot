package app.chatbox.mapper;

import app.chatbox.dto.InboxMsgDTO;
import app.chatbox.model.InboxMsg;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InboxMsgMapper {
    @Mapping(target = "senderId", source = "sender.id")
    @Mapping(target = "inboxId", source = "inbox.id")
    InboxMsgDTO toDTO(InboxMsg entity);
}