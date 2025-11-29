package app.chatbox.repository;

import app.chatbox.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//JPA built-in CRUD:
//


@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    // You can add custom queries if needed, e.g.:
    Optional<AppUser> findByUsername(String username);

}
