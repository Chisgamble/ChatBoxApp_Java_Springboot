package app.chatbox.services;

import app.chatbox.model.GroupMember;
import app.chatbox.repository.GroupMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupMemberService {
    private final GroupMemberRepository repo;

    public List<Long> getGroupMemberIds(Long groupId){
        return repo.findAllMemberIds(groupId);
    }
}
