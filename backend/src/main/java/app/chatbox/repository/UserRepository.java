package app.chatbox.repository;

import app.chatbox.dto.UserMiniDTO;
import app.chatbox.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//JPA built-in CRUD:
//


@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query(value = """
        SELECT u.id, u.email, u.username, u.role
        FROM app_user u
        WHERE u.email = :email
    """, nativeQuery = true)
    UserMiniDTO findMiniByEmail(String email);
}
