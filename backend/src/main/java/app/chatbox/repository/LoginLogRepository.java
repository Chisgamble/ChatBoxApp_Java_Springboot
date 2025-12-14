package app.chatbox.repository;

import app.chatbox.model.LoginLog;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {

    @Query(value = """
        SELECT l.*
        FROM login_log l
        LEFT JOIN app_user u ON l.email = u.email
        WHERE
          (:emailRegex IS NULL OR l.email ~* :emailRegex)
        AND
          (:usernameRegex IS NULL OR u.username ~* :usernameRegex)
        AND
          (
            :status IS NULL
            OR :status = 'all'
            OR (:status = 'success' AND l.isSuccess = true)
            OR (:status = 'fail' AND l.isSuccess = false)
          )
        """, nativeQuery = true)
    List<LoginLog> searchLogs(
            @Param("emailRegex") String emailRegex,
            @Param("usernameRegex") String usernameRegex,
            @Param("status") String status,
            Sort sort
    );
}