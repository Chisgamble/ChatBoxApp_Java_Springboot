package app.chatbox.services;

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

    public void deleteFriend(Long friendId, Long userId) {
        Long a = Math.min(friendId, userId);
        Long b = Math.max(friendId, userId);

        Friend friend = friendRepository
                .findByUserA_IdAndUserB_Id(a, b)
                .orElseThrow(() ->
                        new RuntimeException("Friend not found"));

        friendRepository.delete(friend);
    }

    public List<FriendCardDTO> getAllUserFriends(Long id){
        return friendRepository.getFriendCards(id);
    }
}
