package app.chatbox.repository;

import app.chatbox.dto.InboxMsgDTO;
import app.chatbox.dto.MsgDTO;
import app.chatbox.model.InboxMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface InboxMsgRepository extends JpaRepository<InboxMsg, Long> {
    List<InboxMsg> findTop20ByInbox_IdOrderByCreatedAtDesc(long inboxId);
    Optional<InboxMsg> findTopByInbox_IdOrderByCreatedAtDesc(Long inboxId);
    List<InboxMsg> findAllByIdInAndSender_Id(List<Long> ids, Long senderId);

    @Modifying
    @Transactional
    void deleteByIdInAndSender_Id(
            List<Long> ids,
            Long senderId
    );

    @Query(value = """
        SELECT 
            im.id AS id,
            im.sender_id AS senderId,
            u.username AS senderName,
            im.status AS status,
            im.content AS content
        FROM inbox_message im
        JOIN app_user u ON u.id = im.sender_id
        WHERE im.inbox_id = :inboxId
        ORDER BY im.created_at ASC
    """, nativeQuery = true)
    List<InboxMsgDTO> findAllByInboxIdOrderByCreatedAt(Long inboxId);

    @Query(value = """
        SELECT
            im.id              AS id,
            'INBOX'            AS type,
            im.sender_id       AS senderId,
            u.username         AS senderName,
            im.inbox_id        AS inboxId,
            NULL               AS groupId,
            NULL               AS groupName,
            im.content         AS content,
            im.created_at      AS createdAt
        FROM inbox_message im
        JOIN app_user u ON u.id = im.sender_id
        JOIN inbox i ON i.id = im.inbox_id
        WHERE (:userId = i.userA OR :userId = i.userB)
          AND NOT EXISTS (
              SELECT 1
              FROM user_block b
              WHERE b.blocker_id != :userId
                AND b.blocked_id = :userId
                AND (b.blocker_id = i.userA OR b.blocker_id = i.userB)
      )
    """, nativeQuery = true)
    List<MsgDTO> findInboxMsgsByUser(Long userId);


    @Modifying
    @Query(value = """
        DELETE FROM inbox_message im
        WHERE im.id = :msgId
          AND im.sender_id = :senderId
    """, nativeQuery = true)
    int deleteByIdAndSender(Long msgId, Long senderId);

    void deleteAllByInbox_Id(Long inboxId);


}