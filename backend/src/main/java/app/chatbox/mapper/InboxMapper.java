package app.chatbox.mapper;

import app.chatbox.dto.InboxDTO;
import app.chatbox.model.Inbox;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InboxMapper {

    @Mapping(target = "userAId", source = "userA.id")
    @Mapping(target = "userBId", source = "userB.id")
    @Mapping(target = "userALastSeenMsgId", source = "userALastSeen.id")
    @Mapping(target = "userBLastSeenMsgId", source = "userBLastSeen.id")
    @Mapping(target = "latestMsgs", ignore = true) // filled manually in service
    InboxDTO toDTO(Inbox entity);
}