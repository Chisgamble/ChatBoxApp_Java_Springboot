package app.chatbox.services;

import app.chatbox.dto.response.FriendRequestResDTO;
import app.chatbox.mapper.FriendRequestMapper;
import app.chatbox.repository.FriendRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendRequestService {
    private final FriendRequestRepository repo;
    private final FriendRequestMapper mapper;

    public List<FriendRequestResDTO> getIncomingRequests(Long userId) {
        return repo.findByReceiver_Id(userId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }
}
