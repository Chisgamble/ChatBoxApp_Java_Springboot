package app.chatbox.services;

import app.chatbox.dto.GroupMemberDTO;
import app.chatbox.model.AppUser;
import app.chatbox.model.GroupMember;
import app.chatbox.repository.GroupMemberRepository;
import app.chatbox.repository.GroupRepository;
import app.chatbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<GroupMemberDTO> getAllMembersDTO(Long groupId) {
        return repo.findAllMembersByGroupId(groupId);
    }

    @Transactional
    public void promoteToAdmin(
            Long groupId,
            Long targetUserId,
            Long currentUserId
    ) {
        //Check current user is ADMIN
        boolean isAdmin = repo.existsByGroup_IdAndUser_IdAndRole(
                groupId, currentUserId, "admin"
        );
        if (!isAdmin) {
            throw new AccessDeniedException("Only admin can promote members");
        }

        //Get target member
        GroupMember member = repo.findByGroup_IdAndUser_Id(groupId, targetUserId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Target user not in group"));

        //Promote
        member.setRole("admin");
    }
}
