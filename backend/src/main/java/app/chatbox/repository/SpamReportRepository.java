package app.chatbox.repository;

import app.chatbox.model.SpamReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpamReportRepository extends JpaRepository<SpamReport, Long> {
}