package app.chatbox.services;

import app.chatbox.model.AppUser;
import app.chatbox.model.UserBlock;
import app.chatbox.repository.FriendRepository;
import app.chatbox.repository.UserBlockRepository;
import app.chatbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserBlockService {
    private final UserBlockRepository repo;
    private final FriendRepository friendRepo;
    private final UserRepository userRepo;

    @Transactional
    public void blockUser(Long blockerId, Long blockedId) {
        AppUser blocker = userRepo.findById(blockerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AppUser blocked = userRepo.findById(blockedId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!repo.existsByBlocker_IdAndBlocked_Id(blockerId, blockedId)) {
            UserBlock ub = UserBlock.builder()
                    .blocker(blocker)
                    .blocked(blocked)
                    .build();
            repo.save(ub);
        }

        Long a = Math.min(blockerId, blockedId);
        Long b = Math.max(blockerId, blockedId);

        friendRepo.deleteByUserA_IdAndUserB_Id(a,b);
    }
}
