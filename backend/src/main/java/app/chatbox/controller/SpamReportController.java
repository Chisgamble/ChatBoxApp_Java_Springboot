package app.chatbox.controller;

import app.chatbox.dto.SpamReportListDTO;
import app.chatbox.services.SpamReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/spam-reports")
@RequiredArgsConstructor
public class SpamReportController {

    private final SpamReportService service;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getall")
    public ResponseEntity<SpamReportListDTO> getAll(
            @RequestParam(required = false) List<String> email,
            @RequestParam(required = false) List<String> username,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            @RequestParam(required = false) String status,

            @RequestParam(defaultValue = "time") String sort,
            @RequestParam(defaultValue = "desc") String order
    ) {
        return ResponseEntity.ok(
                service.getAllReports(email, username, startDate, endDate, status, sort, order)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-status/{id}")
    public ResponseEntity<Map<String, String>> updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        service.updateReportStatus(id, status);

        // Return a JSON Object: {"message": "Report status updated successfully."}
        return ResponseEntity.ok(Collections.singletonMap("message", "Report status updated successfully."));
    }
}