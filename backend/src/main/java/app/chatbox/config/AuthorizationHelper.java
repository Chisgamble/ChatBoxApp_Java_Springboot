package app.chatbox.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("authz")
public class AuthorizationHelper {

    public boolean isCurrentUser(long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails user)) {
            return false;
        }

        return user.getId() == userId;  // So sánh session userId với client userId
    }
}
