package app.chatbox.repository;

import app.chatbox.dto.UserMiniDTO;
import app.chatbox.dto.response.StrangerCardResDTO;
import app.chatbox.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//JPA built-in CRUD:
//


@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
    Optional<AppUser> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query(value = """
    select u.email
    from app_user u
    where u.id = :id
    """, nativeQuery = true)
    String findEmailById(Long id);

    @Query(value = """
        SELECT u.id, u.email, u.username, u.role
        FROM app_user u
        WHERE u.email = :email
    """, nativeQuery = true)
    UserMiniDTO findMiniByEmail(String email);

    @Query(value = """
            SELECT u.id, u.username,
            EXISTS (
                    SELECT 1
                    FROM friend_request fr
                    WHERE fr.sender_id = :userId
                      AND fr.receiver_id = u.id
                        ) AS requestSent
                FROM app_user u
                WHERE u.id != :userId
                AND u.is_locked = false
                  AND u.id NOT IN (
                        SELECT CASE
                                WHEN f.userA = :userId THEN f.userB
                                ELSE f.userA
                               END
                        FROM friend f
                        WHERE (f.userA = :userId OR f.userB = :userId)
                    )
                  AND u.id NOT IN (
                        SELECT b.blocker_id
                        FROM user_block b
                        WHERE b.blocked_id = :userId
                    );
    """, nativeQuery = true)
    List<StrangerCardResDTO> findAllStrangerCards (long userId);
}
