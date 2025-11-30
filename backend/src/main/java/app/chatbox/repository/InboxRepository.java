package app.chatbox.repository;

import app.chatbox.model.Inbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InboxRepository extends JpaRepository<Inbox, Long> {
    List<Inbox> findByUserA_IdOrUserB_Id(Long userAId, Long userBId);
}
