package app.chatbox.services;

import app.chatbox.dto.GroupMsgDTO;
import app.chatbox.repository.GroupMsgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupMsgService {
    private final GroupMsgRepository repo;

    public List<GroupMsgDTO> getAllDtoByGroupId(Long groupId){
        return repo.findAllByGroupId(groupId);
    }
}
