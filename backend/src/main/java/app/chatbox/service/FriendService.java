package app.chatbox.service;

import app.chatbox.dto.FriendCardDTO;
//import app.chatbox.mapper.FriendMapper;
import app.chatbox.model.Friend;
import app.chatbox.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
//    private final FriendMapper friendMapper;

    public List<FriendCardDTO> getAllFriends(Long id){
        List<Friend> friends = friendRepository.findByUserA_IdOrUserB_Id(id, id);
        //TODO: add find inbox message between current user and friends then add to FriendCardDTO
        return friendRepository.getFriendCards(id);
    }
}
