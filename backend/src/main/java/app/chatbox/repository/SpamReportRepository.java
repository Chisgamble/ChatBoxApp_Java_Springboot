package app.chatbox.repository;

import app.chatbox.model.SpamReport;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SpamReportRepository extends JpaRepository<SpamReport, Long> {

    @Query(value = """
    SELECT r.* FROM spam_report r
    JOIN app_user u ON r.reported_id = u.id
    WHERE 
        (:emailRegex IS NULL OR CAST(u.email AS text) ~* :emailRegex)
    AND 
        (:usernameRegex IS NULL OR CAST(u.username AS text) ~* :usernameRegex)
    AND 
        (CAST(:start AS TIMESTAMP) IS NULL OR r.created_at >= CAST(:start AS TIMESTAMP))
    AND 
        (CAST(:end AS TIMESTAMP) IS NULL OR r.created_at <= CAST(:end AS TIMESTAMP))
    AND
        (:status IS NULL OR LOWER(r.status) = LOWER(:status))
    """, nativeQuery = true)
    List<SpamReport> searchReports(
            @Param("emailRegex") String emailRegex,       // Pass "gmail|yahoo"
            @Param("usernameRegex") String usernameRegex, // Pass "admin|root"
            @Param("start") Instant start,
            @Param("end") Instant end,
            @Param("status") String status,
            Sort sort
    );
}