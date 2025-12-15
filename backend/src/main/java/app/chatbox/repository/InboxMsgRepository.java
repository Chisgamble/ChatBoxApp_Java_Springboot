package app.chatbox.repository;

import app.chatbox.dto.InboxMsgDTO;
import app.chatbox.model.InboxMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InboxMsgRepository extends JpaRepository<InboxMsg, Long> {
    List<InboxMsg> findTop20ByInbox_IdOrderByCreatedAtDesc(long inboxId);
    List<InboxMsg> findTopByInbox_IdOrderByCreatedAtDesc(Long inboxId);

    @Query(value = """
        SELECT 
            im.id AS id,
            im.sender_id AS senderId,
            im.status AS status,
            im.content AS content
        FROM inbox_message im
        WHERE im.inbox_id = :inboxId
        ORDER BY im.created_at ASC
    """, nativeQuery = true)
    List<InboxMsgDTO> findAllByInboxIdOrderByCreatedAt(Long inboxId);
}