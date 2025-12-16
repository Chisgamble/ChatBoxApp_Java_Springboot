package app.chatbox.services;

import app.chatbox.dto.GroupMsgDTO;
import app.chatbox.model.Group;
import app.chatbox.model.GroupMsg;
import app.chatbox.repository.GroupMsgRepository;
import app.chatbox.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupMsgService {
    private final GroupMsgRepository repo;
    private final GroupRepository groupRepo;

    public List<GroupMsgDTO> getAllDtoByGroupId(Long groupId){
        return repo.findAllByGroupId(groupId);
    }

    @Transactional
    public void deleteMessageBySender(Long groupId, List<Long> msgIds, Long currentUserId) {
        repo.deleteAllByIdInAndSender_Id(msgIds, currentUserId);

        // Update last_msg
        Group group = groupRepo.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Inbox not found"));

        GroupMsg latestMsg = repo.findTopByGroup_IdOrderByCreatedAtDesc(groupId)
                .orElse(null);

        group.setLastMsg(latestMsg);
        groupRepo.save(group);
    }
}
