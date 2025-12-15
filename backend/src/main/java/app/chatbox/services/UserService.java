package app.chatbox.services;

import app.chatbox.dto.MsgDTO;
import app.chatbox.dto.UserMiniDTO;
import app.chatbox.dto.UserListDTO;
import app.chatbox.dto.request.LoginReqDTO;
import app.chatbox.dto.request.RegisterReqDTO;
import app.chatbox.dto.response.RegisterResDTO;
import app.chatbox.dto.response.StrangerCardResDTO;
import app.chatbox.mapper.UserMapper;
import app.chatbox.model.AppUser;
import app.chatbox.model.SpamReport;
import app.chatbox.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final SpamReportRepository spamRepo;
    private final InboxMsgRepository inboxMsgRepo;
    private final GroupMsgRepository groupMsgRepo;

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

    public UserMiniDTO findMiniById(Long id){
        AppUser user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserMiniDTO(user.getId(), user.getEmail(), user.getUsername(), user.getRole());
    }

    public List<StrangerCardResDTO> getAllStrangerCards(Long id) {
        return userRepo.findAllStrangerCards(id);
    }

    public StrangerCardResDTO getById(Long id) {
        AppUser user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserResDTO(user);
    }

    public void reportSpam(Long targetUserId, Long reporterId) {
        AppUser reported = userRepo.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Reported user not found"));

        AppUser reporter = userRepo.findById(reporterId)
                .orElseThrow(() -> new RuntimeException("Reporter not found"));

        SpamReport report = new SpamReport();
        report.setReported(reported);
        report.setReporter(reporter);

        spamRepo.save(report);
    }

    public List<MsgDTO> findAllMsgsByUser(Long userId) {

        List<MsgDTO> result = new ArrayList<>();

        result.addAll(inboxMsgRepo.findInboxMsgsByUser(userId));
        result.addAll(groupMsgRepo.findGroupMsgsByUser(userId));

        result.sort(
                Comparator.comparing(MsgDTO::getCreatedAt)
        );

        return result;
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
