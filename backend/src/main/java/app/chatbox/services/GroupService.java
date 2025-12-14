package app.chatbox.services;

import app.chatbox.dto.GroupCardDTO;
import app.chatbox.dto.GroupMemberDTO;
import app.chatbox.dto.GroupMsgDTO;
import app.chatbox.dto.response.GroupUserResDTO;
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
public class GroupService {
    private final GroupRepository repo;
    private final GroupMemberRepository groupMemberRepo;
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
}