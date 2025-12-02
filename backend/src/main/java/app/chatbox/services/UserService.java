package app.chatbox.services;

import app.chatbox.dto.request.LoginReqDTO;
import app.chatbox.dto.request.RegisterReqDTO;
import app.chatbox.dto.response.LoginResDTO;
import app.chatbox.dto.response.RegisterResDTO;
import app.chatbox.dto.response.UserResDTO;
import app.chatbox.mapper.UserMapper;
import app.chatbox.model.AppUser;
import app.chatbox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final UserMapper mapper;

    public boolean exist(String email){
        return userRepo.existsByEmail(email);
    }

    public List<UserResDTO> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(mapper::toUserResDTO)
                .toList();
    }

    public UserResDTO getById(Long id) {
        AppUser user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapper.toUserResDTO(user);
    }

    public void delete(Long id) {
        userRepo.deleteById(id);
    }

    public RegisterResDTO register(RegisterReqDTO req) {
        // Check if user already exists
        if (exist(req.email())) {
            throw new RuntimeException("Email already registered");
        }

        // Create new user
        AppUser user = new AppUser();
        user.setUsername(req.username());
        user.setEmail(req.email());
        user.setPassword(encoder.encode(req.password())); // hash password
        user.setIsActive(true); // default active
        user.setRole("user");

        AppUser saved = userRepo.save(user);

        return mapper.toRegisterResDTO(saved);
    }

    public LoginResDTO login(LoginReqDTO req) {
        AppUser user = userRepo.findByEmail(req.email())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!encoder.matches(req.password(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        return mapper.toLoginResDTO(user);
    }
}
