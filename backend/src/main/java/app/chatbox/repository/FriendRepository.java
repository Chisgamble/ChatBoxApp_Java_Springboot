package app.chatbox.repository;

import app.chatbox.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findByUserA_IdOrUserB_Id(Long userAId, Long userBId);
}
