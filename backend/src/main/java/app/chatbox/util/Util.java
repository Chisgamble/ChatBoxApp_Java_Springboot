package app.chatbox.util;

import app.chatbox.model.AppUser;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.stream.Collectors;

public class Util {
    public static String buildRegexPattern(List<String> inputs) {
        if (inputs == null || inputs.isEmpty()) return null;
        // Joins list into "john|will|barbra".
        // Postgres ~* operator treats this as "contains john OR contains will..."
        return String.join("|", inputs);
    }
}
