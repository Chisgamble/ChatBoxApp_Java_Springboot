package app.chatbox.repository;

import app.chatbox.dto.GroupMsgDTO;
import app.chatbox.model.GroupMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMsgRepository extends JpaRepository<GroupMsg, Long> {
    @Query(
            value = """
            SELECT 
                gm.id            AS id,
                gm.sender_id     AS senderId,
                u.username       AS senderUsername,
                gm.content       AS content
            FROM group_message gm
            JOIN app_user u ON u.id = gm.sender_id
            WHERE gm.group_id = :groupId
            ORDER BY gm.created_at ASC
        """,nativeQuery = true
    )
    List<GroupMsgDTO> findAllByGroupId(Long groupId);
}