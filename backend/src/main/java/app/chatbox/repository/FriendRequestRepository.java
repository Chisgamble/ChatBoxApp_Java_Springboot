package app.chatbox.repository;

import app.chatbox.dto.response.FriendRequestResDTO;
import app.chatbox.model.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    @Query(value = """
        select fr.id as id,
                fr.sender_id as senderId,
                u.username as senderUsername,
                fr.status as status
        from friend_request fr
        join app_user u on u.id = fr.sender_id
        where fr.receiver_id = :receiverId and fr.status like '%pending%'
    """, nativeQuery = true
    )
    List<FriendRequestResDTO> findFriendRequestCardByReceiver_Id(Long receiverId);

}
