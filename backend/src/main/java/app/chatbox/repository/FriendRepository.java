package app.chatbox.repository;

import app.chatbox.dto.FriendCardDTO;
import app.chatbox.model.AppUser;
import app.chatbox.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findByUserA_IdOrUserB_Id(Long userAId, Long userBId);
    Friend findByUserA_IdAndUserB_Id(Long userAId, Long userBId);
    Optional<Friend> findByUserAAndUserB(AppUser userA, AppUser userB);

    @Query(value = """
        SELECT 
            f.id AS id,
            COALESCE(m.sender_id, 0) AS senderId,
            COALESCE(i.id, 0) AS inboxId,
            u.username AS username,
            u.is_active AS isActive,
            m.content AS content
        FROM friend f
        JOIN app_user u 
            ON (CASE 
                    WHEN f.userA = :currentUserId THEN f.userB 
                    ELSE f.userA 
                END) = u.id
        LEFT JOIN inbox i 
            ON (
                (i.userA = :currentUserId AND i.userB = u.id) OR
                (i.userA = u.id AND i.userB = :currentUserId)
            )
        LEFT JOIN LATERAL (
            SELECT im.sender_id, im.content
            FROM inbox_message im
            WHERE im.inbox_id = i.id
            ORDER BY im.created_at DESC
            LIMIT 1
        ) m ON TRUE
        WHERE f.userA = :currentUserId OR f.userB = :currentUserId
        ORDER BY f.updated_at DESC;
    """, nativeQuery = true)
    List<FriendCardDTO> getFriendCards(long currentUserId);

}
