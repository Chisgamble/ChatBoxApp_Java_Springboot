package app.chatbox.services;

import app.chatbox.dto.request.UpdateFriendRequestReqDTO;
import app.chatbox.dto.response.FriendRequestResDTO;
import app.chatbox.dto.response.UpdateFriendRequestResDTO;
import app.chatbox.model.AppUser;
import app.chatbox.model.Friend;
import app.chatbox.model.FriendRequest;
import app.chatbox.repository.FriendRepository;
import app.chatbox.repository.FriendRequestRepository;
import app.chatbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendRequestService {
    private final FriendRequestRepository repo;
    private final FriendRepository friendRepo;
    private final UserRepository userRepo;

    public List<FriendRequestResDTO> getIncomingRequests(Long userId) {
        return repo.findFriendRequestCardByReceiver_Id(userId);
    }

    public UpdateFriendRequestResDTO updateFriendRequest(UpdateFriendRequestReqDTO req, Long requestId){
         FriendRequest friendRequest = repo.findById(requestId)
                 .orElseThrow(() -> new RuntimeException("FriendRequest not found"));

        // 2. Validate status
        if (!req.getStatus().equals("accepted") && !req.getStatus().equals("rejected")) {
            throw new RuntimeException("Invalid status");
        }

        friendRequest.setStatus(req.getStatus());

         repo.save(friendRequest);

         UpdateFriendRequestResDTO res = null;

        if (req.getStatus().equals("accepted")) {

            AppUser user = userRepo.findById(req.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            AppUser sender = userRepo.findById(req.getSenderId())
                    .orElseThrow(() -> new RuntimeException("Sender not found"));

            // Normalize pair for uniqueness (userA < userB)
            AppUser userA = user.getId() < sender.getId() ? user : sender;
            AppUser userB = user.getId() < sender.getId() ? sender : user;

            // 5. Check if friend already exists (safety)
            friendRepo.findByUserAAndUserB(userA, userB)
                .ifPresent(f -> {
                    throw new RuntimeException("Friend record already exists");
                });

            // 6. Create new Friend
            Friend friend = Friend.builder()
                .userA(userA)
                .userB(userB)
                .lastMsg(null)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            friendRepo.save(friend);

            res = new UpdateFriendRequestResDTO(friendRequest.getId(), sender.getId(), sender.getUsername(), sender.getIsActive());
        }else{
            AppUser sender = userRepo.findById(req.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
            res = new UpdateFriendRequestResDTO(friendRequest.getId(), sender.getId(), sender.getUsername(), null);
        }
        return res;
    }
}
