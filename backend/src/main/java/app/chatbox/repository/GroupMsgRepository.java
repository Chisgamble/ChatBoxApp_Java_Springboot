package app.chatbox.repository;

import app.chatbox.model.GroupMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMsgRepository extends JpaRepository<GroupMsg, Long> {
}