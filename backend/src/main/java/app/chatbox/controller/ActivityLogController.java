package app.chatbox.controller;

import app.chatbox.dto.ActivityListDTO;
import app.chatbox.services.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityLogController {
    private final ActivityLogService service;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getall/data")
    public ResponseEntity<ActivityListDTO> getAllUserActivity(
            @RequestParam(required = false) List<String> usernameFilter,
            @RequestParam(required = false) String activityType, // e.g., "open", "with-one"
            @RequestParam(required = false) String comparison,   // e.g., "<", ">", "="
            @RequestParam(required = false) String count,        // e.g., "10"
            @RequestParam(required = false, defaultValue = "username") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDir
    ) {
        ActivityListDTO activityData = service.getUserActivityData(
                usernameFilter,
                activityType,
                comparison,
                count,
                sortBy,
                sortDir
        );
        return ResponseEntity.ok(activityData);
    }
}