package app.chatbox.services;

import app.chatbox.dto.UserMiniDTO;
import app.chatbox.dto.FriendCardListDTO;
import app.chatbox.dto.UserDTO;
import app.chatbox.dto.UserListDTO;
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
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmailService emailService;

    public boolean exist(String email){
        return userRepo.existsByEmail(email);
    }

    public UserListDTO getAllUsersAndData(){
        List<AppUser> users = userRepo.findAll();
        return new UserListDTO(userMapper.toAppUserDTOList(users));
    }

    public List<UserResDTO> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(userMapper::toUserResDTO)
                .toList();
    }

    public UserResDTO getById(Long id) {
        AppUser user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserResDTO(user);
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
        user.setPassword(passwordEncoder.encode(req.password())); // hash password
        user.setIsActive(true); // default active
        user.setRole("user");

        AppUser saved = userRepo.save(user);

        return userMapper.toRegisterResDTO(saved);
    }

    public UserMiniDTO login(LoginReqDTO req) {
        UserMiniDTO user = userRepo.findMiniByEmail(req.email());

        return user;
    }

    public void resetPasswordAndSendEmail(String email) {

        AppUser user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 1) Tạo mật khẩu random
        String newPass = generateRandomPassword(10);

        // 2) encode và save
        user.setPassword(passwordEncoder.encode(newPass));
        userRepo.save(user);

        // 3) gửi email
        emailService.sendMail(email,
                "Password Reset",
                "Your new password for ChatBox's account is: " + newPass
        );
    }

    public void updatePassword(Long userId, String newPassword) {

        AppUser user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }

    private String generateRandomPassword(int length) {
        return java.util.UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, length);
    }
}
