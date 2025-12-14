package app.chatbox.services;

import app.chatbox.dto.GroupMemberDTO;
import app.chatbox.model.AppUser;
import app.chatbox.model.GroupMember;
import app.chatbox.repository.GroupMemberRepository;
import app.chatbox.repository.GroupRepository;
import app.chatbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupMemberService {
    private final GroupMemberRepository repo;
    private final GroupRepository groupRepository;
    private final UserRepository userRepo;

    public GroupMemberDTO getMemberDtoByGroupIdAndUserId(Long groupId, Long userId){
        if (!groupRepository.existsById(groupId))
            throw new RuntimeException("Group not exist");

        GroupMember mem = repo.findByGroup_IdAndUser_Id(groupId, userId)
                .orElseThrow(() -> new RuntimeException("GroupMember not found"));
        AppUser user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new GroupMemberDTO(mem.getId(), user.getId(), groupId, user.getEmail(), user.getUsername(), mem.getRole());
    }

    public List<Long> getGroupMemberIds(Long groupId){
        return repo.findAllMemberIds(groupId);
    }
}
