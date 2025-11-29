package app.chatbox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InboxMsg extends JpaRepository<app.chatbox.model.InboxMsg,  Long> {

}
