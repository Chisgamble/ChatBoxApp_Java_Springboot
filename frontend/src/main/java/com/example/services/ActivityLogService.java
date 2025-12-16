package com.example.services;

import com.example.api.ActivityLogApi;
import com.example.dto.ActivityDTO;
import com.example.dto.ActivityListDTO;

import java.util.Collections;
import java.util.List;

public class ActivityLogService {
    private final ActivityLogApi api = new ActivityLogApi();

    public List<ActivityDTO> getAllUserActivity(
            List<String> usernameFilters,
            String activityType,
            String comparison,
            String count,
            String sortBy,
            String sortDir
    ) {
        // Business logic: Prepare the username filter string
        // Assuming the backend expects a single string pattern, we take the first filter.
        String usernameQuery = usernameFilters.isEmpty() ? null : usernameFilters.get(0);

        // Ensure comparison and count are null if the filter is inactive.
        if (activityType == null || activityType.isEmpty() || comparison == null || comparison.isEmpty() || count == null || count.isEmpty()) {
            activityType = null;
            comparison = null;
            count = null;
        }

        try {
            ActivityListDTO wrapper = api.getAllUserActivity(
                    usernameQuery,
                    activityType,
                    comparison,
                    count,
                    sortBy,
                    sortDir
            );

            // Extract the list from the wrapper DTO
            return wrapper.activities();

        } catch (RuntimeException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}