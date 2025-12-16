package com.example.services.admin;

import com.example.api.admin.SpamReportApi;
import com.example.dto.SpamReportDTO;
import com.example.dto.SpamReportListDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SpamReportService {

    private final SpamReportApi api;

    public SpamReportService() {
        this.api = new SpamReportApi();
    }

    public List<List<String>> getAll(
            String email,
            String username,
            LocalDate startDate,
            LocalDate endDate,
            String status,
            String sort,
            String order
    ) {
        SpamReportListDTO response = api.getAll(email, username, startDate, endDate, status, sort, order);

        List<List<String>> data = new ArrayList<>();
        if (response != null && response.reports() != null) {
            for (SpamReportDTO r : response.reports()) {
                List<String> row = new ArrayList<>();

                // --- Visible Columns in UI ---
                row.add(r.reportedUsername());           // 0: Username
                row.add(r.createdAt());                  // 1: Time
                row.add(r.reportedEmail());              // 2: Email
                row.add("");                             // 3: Button Placeholder

                // --- Hidden / Logic Columns ---
                row.add(String.valueOf(r.id()));         // 4: Report ID (Needed for status update)
                row.add(String.valueOf(r.reportedId())); // 5: Reported User ID (Needed for Lock)
                row.add(String.valueOf(r.isLocked()));   // 6: IsLocked (For toggle logic)

                // --- New Status Field ---
                String statusVal = (r.status() == null) ? "pending" : r.status();
                row.add(statusVal);                      // 7: Status (pending/done)

                data.add(row);
            }
        }
        return data;
    }

    public void updateStatus(Long reportId, String status) {
        api.updateStatus(reportId, status);
    }
}