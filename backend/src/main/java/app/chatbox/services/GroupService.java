package app.chatbox.services;

import app.chatbox.dto.*;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static app.chatbox.mapper.SpamReportMapper.FORMATTER;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository repo;
    private final GroupMemberRepository groupMemberRepo;
    private final GroupMsgRepository groupMsgRepo;
    private final UserRepository userRepo;

    private final GroupMemberService groupMemberService;
    private final GroupMsgService groupMsgService;


    public GroupListDataDTO getAllGroupData(
            String nameFilter,
            String startDate,
            String endDate,
            String sortBy,
            String sortDir
    ) {
        String namePattern = (nameFilter != null && !nameFilter.isEmpty()) ? "%" + nameFilter + "%" : null;

        List<Object[]> results = repo.findGroupListDataRaw(
                namePattern,
                startDate,
                endDate,
                sortBy,
                sortDir
        );

        // Map raw results to List<GroupDataDTO> (using String for time)
        List<GroupDataDTO> groupDataList = results.stream()
                .map(row -> {
                    Timestamp timestamp = (Timestamp) row[2];
                    String createdAtString = null; // Prepare for String output

                    if (timestamp != null) {
                        // Convert Timestamp to LocalDateTime, then format to String
                        LocalDateTime localDateTime = timestamp.toLocalDateTime();
                        createdAtString = localDateTime.format(FORMATTER);
                    }

                    return new GroupDataDTO(
                            ((Number) row[0]).longValue(), // id
                            (String) row[1],               // groupName
                            createdAtString                // createdAt (Formatted String)
                    );
                })
                .collect(Collectors.toList());

        // Wrap the list in the final wrapper DTO
        return new GroupListDataDTO(groupDataList);
    }

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