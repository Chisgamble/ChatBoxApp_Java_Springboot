package app.chatbox.repository;

import app.chatbox.dto.UserMiniDTO;
import app.chatbox.dto.response.StrangerCardResDTO;
import app.chatbox.model.AppUser;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//JPA built-in CRUD:
//


@Repository
public interface UserRepository extends JpaRepository<AppUser, Long>, JpaSpecificationExecutor<AppUser> {
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
    SELECT *
    FROM app_user u
    WHERE
      (:usernameRegex IS NULL OR LOWER(u.username) ~ :usernameRegex)
    AND
      (:nameRegex IS NULL OR LOWER(u.name) ~ :nameRegex)
    AND
      (
        :status IS NULL
        OR :status = 'all'
        OR (:status = 'active' AND u.is_active = true)
        OR (:status = 'offline' AND u.is_active = false)
      )
    ORDER BY
      CASE WHEN :sort = 'username' THEN u.username END ASC,
      CASE WHEN :sort = 'name' THEN u.name END ASC,
      CASE WHEN :sort = 'email' THEN u.email END ASC
    """,
            nativeQuery = true)
    List<AppUser> findUsersAsc(
            @Param("usernameRegex") String usernameRegex,
            @Param("nameRegex") String nameRegex,
            @Param("status") String status,
            @Param("sort") String sort
    );

    @Query(value = """
        SELECT *
        FROM app_user u
        WHERE
          (:usernameRegex IS NULL OR u.username ~* :usernameRegex)
        AND
          (:nameRegex IS NULL OR u.name ~* :nameRegex)
        AND
          (
            :status = 'all'
            OR (:status = 'active' AND u.is_active = true)
            OR (:status = 'offline' AND u.is_active = false)
          )
        AND
            (
            :role = 'all'
            OR (LOWER(:role) = 'admin' AND LOWER(u.role) = 'admin')
            OR (LOWER(:role) = 'user' AND LOWER(u.role) = 'user')
            )
        """, nativeQuery = true)
    List<AppUser> searchUsers(
            @Param("usernameRegex") String usernameRegex,
            @Param("nameRegex") String nameRegex,
            @Param("status") String status,
            @Param("role") String role,
            Sort sort
    );

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

    @Query(value = """
        SELECT * FROM app_user u
        WHERE
          (:username IS NULL OR LOWER(u.username) ~* LOWER(:username))
        AND
          (:email IS NULL OR u.email ~* :email)
        AND
          (CAST(:startDate AS timestamp) IS NULL OR u.created_at >= :startDate)
        AND
          (CAST(:endDate AS timestamp) IS NULL OR u.created_at <= :endDate)
        """, nativeQuery = true)
        // ^ IMPORTANT: Removed "ORDER BY u.created_at DESC" from string
    List<AppUser> findNewUsers(
            @Param("username") String username,
            @Param("email") String email,
            @Param("startDate") java.time.Instant startDate,
            @Param("endDate") java.time.Instant endDate,
            Sort sort
    );

    // 1. FOR NEW USERS
    @Query(value = """
        SELECT 
            CAST(EXTRACT(MONTH FROM created_at) AS INTEGER) as month, 
            COUNT(*) as count 
        FROM app_user 
        WHERE EXTRACT(YEAR FROM created_at) = :year 
        GROUP BY month
        """, nativeQuery = true)
    List<Object[]> findNewUserCounts(@Param("year") int year);

}
