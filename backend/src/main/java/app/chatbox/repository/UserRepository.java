package app.chatbox.repository;

import app.chatbox.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // You can add custom queries if needed, e.g.:
    Optional<User> findByUsername(String username);

}
