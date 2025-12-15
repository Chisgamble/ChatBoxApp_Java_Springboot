package app.chatbox.util;

import app.chatbox.model.AppUser;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Util {
    public static String buildRegexPattern(List<String> inputs) {
        if (inputs == null || inputs.isEmpty()) return null;
        // Joins list into "john|will|barbra".
        // Postgres ~* operator treats this as "contains john OR contains will..."
        return String.join("|", inputs);
    }

    public static List<Long> mapToMonthlyList(List<Object[]> rawData) {
        List<Long> monthlyData = new ArrayList<>(Collections.nCopies(12, 0L));

        for (Object[] row : rawData) {
            int month = ((Number) row[0]).intValue();
            long count = ((Number) row[1]).longValue();
            if (month >= 1 && month <= 12) {
                monthlyData.set(month - 1, count);
            }
        }
        return monthlyData;
    }
}
