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

    @Query("""
        SELECT r FROM SpamReport r
        JOIN r.reported u
        WHERE 
            (:email IS NULL OR u.email LIKE :email)
        AND 
            (:username IS NULL OR u.username LIKE :username)
        AND 
            (cast(:start as timestamp) IS NULL OR r.createdAt >= :start)
        AND 
            (cast(:end as timestamp) IS NULL OR r.createdAt <= :end)
        AND
            (:status IS NULL OR r.status = :status)
        """)
    List<SpamReport> searchReports(
            @Param("email") String email,       // Expecting "%abc%" or NULL
            @Param("username") String username, // Expecting "%abc%" or NULL
            @Param("start") Instant start,
            @Param("end") Instant end,
            @Param("status") String status,
            Sort sort
    );
}