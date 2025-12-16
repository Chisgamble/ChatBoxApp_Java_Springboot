package app.chatbox.services;

import app.chatbox.config.CustomUserDetails;
import app.chatbox.model.AppUser;
import app.chatbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserDetailService implements UserDetailsService {

    private final UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) {
        AppUser user = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));

        if (user.getIs_locked()) {
            throw new LockedException("Account is locked");
        }

        return new CustomUserDetails(user);
    }
}
