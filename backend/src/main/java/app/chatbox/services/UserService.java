package app.chatbox.services;

import app.chatbox.dto.UserMiniDTO;
import app.chatbox.dto.UserDTO;
import app.chatbox.dto.UserListDTO;
import app.chatbox.dto.request.AdminCreateOrUpdateUserReqDTO;
import app.chatbox.dto.request.LoginReqDTO;
import app.chatbox.dto.request.RegisterReqDTO;
import app.chatbox.dto.response.RegisterResDTO;
import app.chatbox.dto.response.UserResDTO;
import app.chatbox.mapper.UserMapper;
import app.chatbox.model.AppUser;
import app.chatbox.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import app.chatbox.util.Util;

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


    public UserListDTO getAllUsersAndData(
            String sort,
            String order,
            List<String> usernames,
            List<String> names,
            String status
    ) {
        // 1. Handle Defaults
        String sortInput = (sort == null || sort.isEmpty()) ? "username" : sort;
        String orderInput = (order == null || order.isEmpty()) ? "asc" : order;
        String statusInput = (status == null || status.isEmpty()) ? "all" : status;

        // 2. Build Regex for filtering
        String usernameRegex = Util.buildRegexPattern(usernames);
        String nameRegex = Util.buildRegexPattern(names);

        // 3. Map Frontend Sort Keys to Database Column Names (snake_case)
        String dbSortColumn = switch (sortInput) {
            case "age" -> "created_at"; // Maps "age" to Postgres "created_at"
            case "name" -> "name";
            case "email" -> "email";
            default -> "username";
        };

        // 4. Build the Sort Object
        Sort.Direction direction;
        if(sortInput.equals("age")){
            direction = orderInput.equalsIgnoreCase("desc")
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;
        }
        else{
            direction = orderInput.equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
        }

        Sort sortObj = Sort.by(direction, dbSortColumn);

        // 5. Query Repository
        // Spring Data JPA automatically appends the "ORDER BY ..." from sortObj to your native query
        List<AppUser> users = userRepo.searchUsers(
                usernameRegex,
                nameRegex,
                statusInput,
                sortObj
        );

        // 6. Return DTO
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

    public UserDTO getUserDataById(Long id) {
        AppUser user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toAppUserDTO(user);
    }

    public UserResDTO createUser(AdminCreateOrUpdateUserReqDTO req) {
        if (userRepo.existsByEmail(req.email())) {
            throw new RuntimeException("Email already exist");
        }
//        if (userRepo.findByUsername(req.username()) != null) {
//            throw new RuntimeException("Username already taken");
//        }

        AppUser user = new AppUser();
        user.setUsername(req.username());
        user.setEmail(req.email());
        user.setPassword(passwordEncoder.encode(req.password()));

        user.setName(req.name());
        user.setAddress(req.address());
        user.setGender(req.gender());
        user.setBirthday(req.birthday()); // Matches SQL 'birthday'

        // Defaults
        user.setRole(req.role() != null ? req.role() : "user");
        user.setIsActive(true);
        user.setIsLocked(false);

        AppUser saved = userRepo.save(user);
        return userMapper.toUserResDTO(saved);
    }

    public UserResDTO updateUserInfo(Long userId, AdminCreateOrUpdateUserReqDTO req) {
        AppUser user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (req.username() != null) user.setUsername(req.username());
        if (req.name() != null) user.setName(req.name());
        if (req.address() != null) user.setAddress(req.address());
        if (req.gender() != null) user.setGender(req.gender());
        if (req.role() != null) user.setRole(req.role());
        if (req.birthday() != null) user.setBirthday(req.birthday());
        if (req.email() != null) {
            // Optional: check uniqueness if email is changing
            if (!user.getEmail().equals(req.email()) && userRepo.existsByEmail(req.email())) {
                throw new RuntimeException("Email already in use");
            }
            user.setEmail(req.email());
        }

        AppUser saved = userRepo.save(user);
        return userMapper.toUserResDTO(saved);
    }

    @Transactional
    public void updateLockStatus(Long userId, boolean isLocked) {
        AppUser user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsLocked(isLocked);
        userRepo.save(user);
    }

    public void delete(Long id) {
        if (!userRepo.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
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
