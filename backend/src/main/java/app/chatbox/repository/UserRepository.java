package app.chatbox.repository;

import app.chatbox.dto.UserMiniDTO;
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
        """, nativeQuery = true)
    List<AppUser> searchUsers(
            @Param("usernameRegex") String usernameRegex,
            @Param("nameRegex") String nameRegex,
            @Param("status") String status,
            Sort sort
    );
}
