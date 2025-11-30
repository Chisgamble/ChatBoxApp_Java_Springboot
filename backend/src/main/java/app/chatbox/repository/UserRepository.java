package app.chatbox.repository;

import app.chatbox.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//JPA built-in CRUD:
//


@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);
}
