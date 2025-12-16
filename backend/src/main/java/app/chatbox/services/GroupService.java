package app.chatbox.services;

import app.chatbox.dto.AddMemberCardDTO;
import app.chatbox.dto.GroupCardDTO;
import app.chatbox.dto.GroupMemberDTO;
import app.chatbox.dto.GroupMsgDTO;
import app.chatbox.dto.request.CreateGroupReqDTO;
import app.chatbox.dto.response.GroupUserResDTO;
import app.chatbox.model.AppUser;
import app.chatbox.model.Group;
import app.chatbox.model.GroupMember;
import app.chatbox.repository.GroupMemberRepository;
import app.chatbox.repository.GroupMsgRepository;
import app.chatbox.repository.GroupRepository;
import app.chatbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository repo;
    private final GroupMemberRepository groupMemberRepo;
    private final GroupMsgRepository groupMsgRepo;
    private final UserRepository userRepo;

    private final GroupMemberService groupMemberService;
    private final GroupMsgService groupMsgService;

    public List<GroupCardDTO> getAllGroups(Long userId){
        return repo.getAllCardByUserId(userId);
    }

    public GroupUserResDTO getUserGroupInfo(Long groupId, Long userId){
        if (!repo.existsById(groupId))
            throw new RuntimeException("Group not exist");

        GroupMemberDTO member = groupMemberService.getMemberDtoByGroupIdAndUserId(groupId, userId);

        List<GroupMsgDTO> msgs = groupMsgService.getAllDtoByGroupId(groupId);

        return new GroupUserResDTO(member, msgs);
    }

    public void deleteGroup(Long groupId, Long userId) {
        // Check if user is admin of the group
        GroupMember member = groupMemberRepo.findByGroup_IdAndUser_Id(groupId, userId)
                .orElseThrow(() -> new RuntimeException("Not a group member"));

        if (!member.getRole().equals("admin")) {
            throw new RuntimeException("Only admin can delete group");
        }

        // Delete group
        repo.deleteById(groupId);
    }

    @Transactional
    public void deleteAllMessages(Long groupId) {
        groupMsgRepo.deleteAllByGroup_Id(groupId);
    }

    @Transactional
    public void leaveGroup(Long groupId, Long userId) {
        groupMemberRepo.deleteByGroup_IdAndUser_Id(groupId, userId);
    }

    @Transactional
    public GroupCardDTO createGroup(CreateGroupReqDTO req, Long creatorId) {

        AppUser creator = userRepo.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group group = Group.builder()
                .name(req.getGroupName())
                .build();

        repo.save(group);

        // creator = admin
        groupMemberRepo.save(
                GroupMember.builder()
                        .group(group)
                        .user(creator)
                        .role("admin")
                        .build()
        );

        // add other members
        if (req.getMemberIds() != null) {
            req.getMemberIds().forEach(uid -> {
                if (!uid.equals(creatorId)) {
                    AppUser user = userRepo.getReferenceById(uid);
                    groupMemberRepo.save(
                            GroupMember.builder()
                                    .group(group)
                                    .user(user)
                                    .role("member")
                                    .build()
                    );
                }
            });
        }

        return new GroupCardDTO(
                group.getId(),
                null,
                group.getName(),
                null
        );
    }

    public List<AddMemberCardDTO> getAllFriendsNotInGroup(Long groupId, Long currentUserId){
        if (!repo.existsById(groupId)) {
            throw new RuntimeException("Group not found");
        }
        return repo.getAllFriendsNotInGroup(groupId, currentUserId);
    }

    @Transactional
    public void addMember(Long groupId, Long requesterId, Long newUserId) {

        GroupMember admin = groupMemberRepo
                .findByGroup_IdAndUser_Id(groupId, requesterId)
                .orElseThrow(() -> new RuntimeException("Not a member"));

        if (!admin.getRole().equals("admin"))
            throw new RuntimeException("Only admin can add member");

        if (groupMemberRepo.existsByGroup_IdAndUser_Id(groupId, newUserId))
            throw new RuntimeException("User already in group");

        Group group = repo.getReferenceById(groupId);
        AppUser user = userRepo.getReferenceById(newUserId);

        groupMemberRepo.save(
                GroupMember.builder()
                        .group(group)
                        .user(user)
                        .role("member")
                        .build()
        );
    }

    @Transactional
    public void addMembers(Long groupId, Long adminId, List<Long> userIds) {

        GroupMember admin = groupMemberRepo
                .findByGroup_IdAndUser_Id(groupId, adminId)
                .orElseThrow(() -> new RuntimeException("Not in group"));

        if (!admin.getRole().equals("admin")) {
            throw new RuntimeException("Only admin can add members");
        }

        Group group = repo.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<AppUser> users = userRepo.findAllById(userIds);

        for (AppUser u: users) {
            if (groupMemberRepo.existsByGroup_IdAndUser_Id(groupId, u.getId())) continue;

            GroupMember m = new GroupMember();
            m.setGroup(group);
            m.setUser(u);
            m.setRole("member");

            groupMemberRepo.save(m);
        }
    }


    @Transactional
    public void removeMember(Long groupId, Long adminId, Long targetUserId) {

        GroupMember admin = groupMemberRepo
                .findByGroup_IdAndUser_Id(groupId, adminId)
                .orElseThrow(() -> new RuntimeException("Not a member"));

        if (!admin.getRole().equals("admin"))
            throw new RuntimeException("Only admin can remove member");

        groupMemberRepo.deleteByGroup_IdAndUser_Id(groupId, targetUserId);
    }

    @Transactional
    public GroupCardDTO changeGroupName(Long groupId, Long userId, String newName) {

        GroupMember member = groupMemberRepo
                .findByGroup_IdAndUser_Id(groupId, userId)
                .orElseThrow(() -> new RuntimeException("Not a member"));

        if (!member.getRole().equals("admin"))
            throw new RuntimeException("Only admin can rename group");

        Group group = repo.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        group.setName(newName);

        return new GroupCardDTO(
                group.getId(),
                userId,
                group.getName(),
                group.getLastMsg() != null ? group.getLastMsg().getContent() : null
        );
    }



}