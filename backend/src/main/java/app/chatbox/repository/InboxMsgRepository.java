package app.chatbox.repository;

import app.chatbox.model.InboxMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InboxMsgRepository extends JpaRepository<InboxMsg, Long> {
    List<InboxMsg> findTop20ByInbox_IdOrderByCreatedAtDesc(long inboxId);
}