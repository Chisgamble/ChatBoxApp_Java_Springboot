package app.chatbox.services;

import app.chatbox.dto.SpamReportDTO;
import app.chatbox.dto.SpamReportListDTO;
import app.chatbox.mapper.SpamReportMapper;
import app.chatbox.model.SpamReport;
import app.chatbox.repository.SpamReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpamReportService {

    private final SpamReportRepository reportRepo;
    private final SpamReportMapper mapper;

    public SpamReportListDTO getAllReports(
            String email,
            String username,
            LocalDate startDate,
            LocalDate endDate,
            String status,
            String sortBy,
            String order
    ) {
        ZoneId zone = ZoneId.systemDefault();

        Instant startInstant = (startDate != null)
                ? startDate.atStartOfDay(zone).toInstant()
                : null;

        Instant endInstant = (endDate != null)
                ? endDate.atTime(LocalTime.MAX).atZone(zone).toInstant()
                : null;

        // --- FIX: Add Wildcards here instead of inside SQL ---
        String emailPattern = (email != null && !email.isBlank()) ? "%" + email + "%" : null;
        String usernamePattern = (username != null && !username.isBlank()) ? "%" + username + "%" : null;
        String statusFilter = (status != null && !status.isBlank()) ? status : null;

        // Sort Logic
        String sortField = "createdAt";
        if ("username".equalsIgnoreCase(sortBy)) {
            sortField = "reported.username";
        }

        Sort.Direction direction = (order != null && order.equalsIgnoreCase("asc"))
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Sort sortObj = Sort.by(direction, sortField);

        // Execute Search
        List<SpamReport> reports = reportRepo.searchReports(
                emailPattern,    // Passed with %
                usernamePattern, // Passed with %
                startInstant,
                endInstant,
                statusFilter,
                sortObj
        );

        List<SpamReportDTO> dtos = reports.stream()
                .map(mapper::toDTO)
                .toList();

        return new SpamReportListDTO(dtos);
    }

    @Transactional
    public void updateReportStatus(Long reportId, String newStatus) {
        SpamReport report = reportRepo.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found with id: " + reportId));

        report.setStatus(newStatus);
        reportRepo.save(report);
    }
}