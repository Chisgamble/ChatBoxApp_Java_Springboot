package app.chatbox.repository;

import app.chatbox.dto.GroupMsgDTO;
import app.chatbox.dto.MsgDTO;
import app.chatbox.model.GroupMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupMsgRepository extends JpaRepository<GroupMsg, Long> {
    Optional<GroupMsg> findTopByGroup_IdOrderByCreatedAtDesc(Long groupId);

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

    @Query(value = """
        SELECT
            gm.id              AS id,
            'GROUP'            AS type,
            gm.sender_id       AS senderId,
            u.username         AS senderName,
            NULL               AS inboxId,
            g.id               AS groupId,
            g.group_name       AS groupName,
            gm.content         AS content,
            gm.created_at      AS createdAt
        FROM group_message gm
        JOIN chat_group g ON g.id = gm.group_id
        JOIN group_member m ON m.group_id = g.id
        JOIN app_user u ON u.id = gm.sender_id
        WHERE m.user_id = :userId
    """, nativeQuery = true)
    List<MsgDTO> findGroupMsgsByUser(Long userId);


    //    @Query(
//        value = "DELETE FROM group_message WHERE group_id = :groupId",
//        nativeQuery = true
//    )
    void deleteAllByGroup_Id(Long groupId);
    void deleteByIdAndSender_Id(Long id, Long senderId);
    void deleteAllByIdInAndSender_Id(List<Long> msgIds, Long senderId);
}