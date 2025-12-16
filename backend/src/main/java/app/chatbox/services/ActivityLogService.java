package app.chatbox.services;

import app.chatbox.dto.ActivityDTO;
import app.chatbox.dto.ActivityListDTO;
import app.chatbox.model.ActivityLog;
import app.chatbox.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityLogService {
    private final ActivityLogRepository repo;

    // Formatter for DTO presentation
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ActivityListDTO getUserActivityData(
            String usernameFilter,
            String activityType,
            String comparison,
            String count,
            String sortBy,
            String sortDir
    ) {
        String usernamePattern = (usernameFilter != null && !usernameFilter.isEmpty()) ? "%" + usernameFilter + "%" : null;

        List<Object[]> results = repo.findUserActivityDataRaw(
                usernamePattern,
                activityType,
                comparison,
                count,
                sortBy,
                sortDir
        );

        // Process raw results into ActivityDTOs
        List<ActivityDTO> activityDTOs = results.stream()
                .map(row -> {
                    Timestamp createdTimestamp = (Timestamp) row[2];
                    String createdAtString = (createdTimestamp != null)
                            ? createdTimestamp.toLocalDateTime().format(formatter)
                            : "N/A";

                    return new ActivityDTO(
                            ((Number) row[0]).longValue(), // userId
                            (String) row[1],               // username
                            createdAtString,               // createdAt
                            ((Number) row[3]).longValue(), // openCount
                            ((Number) row[4]).longValue(), // chatOneCount
                            ((Number) row[5]).longValue()  // chatGroupCount
                    );
                })
                .collect(Collectors.toList());

        return new ActivityListDTO(activityDTOs);
    }
}