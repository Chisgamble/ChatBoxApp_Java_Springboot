package app.chatbox.repository;

import app.chatbox.dto.GroupCardDTO;
import app.chatbox.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query(
            value = """
            SELECT
                g.id              AS id,
                gm.sender_id      AS senderId,
                g.group_name            AS groupname,
                gm.content        AS last_msg
            FROM group_member m
            JOIN chat_group g
                ON m.group_id = g.id
            LEFT JOIN group_message gm
                ON g.last_msg = gm.id
            WHERE m.user_id = :userId
            ORDER BY g.updated_at DESC
        """, nativeQuery = true)
    List<GroupCardDTO> getAllCardByUserId(Long userId);

}
