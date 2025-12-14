package app.chatbox.service;

import app.chatbox.dto.LoginLogDTO;
import app.chatbox.dto.LoginLogListDTO;
import app.chatbox.mapper.LoginLogMapper;
import app.chatbox.model.AppUser;
import app.chatbox.model.LoginLog;
import app.chatbox.repository.LoginLogRepository;
import app.chatbox.repository.UserRepository;
import app.chatbox.util.Util; // Assuming this is where buildRegexPattern is
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginLogService {

    private final LoginLogRepository loginLogRepo;
    private final UserRepository userRepo;
    private final LoginLogMapper loginLogMapper;

    public LoginLogListDTO getAllLogsAndData(
            List<String> emails,
            List<String> usernames,
            String status, // <--- NEW PARAMETER
            String order
    ) {
        String emailRegex = Util.buildRegexPattern(emails);
        String usernameRegex = Util.buildRegexPattern(usernames);

        // Default status to 'all' if missing
        String statusInput = (status == null || status.isEmpty()) ? "all" : status.toLowerCase();

        Sort.Direction direction = (order != null && order.equalsIgnoreCase("desc"))
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sortObj = Sort.by(direction, "created_at");

        // Pass statusInput to the repo
        List<LoginLog> logs = loginLogRepo.searchLogs(emailRegex, usernameRegex, statusInput, sortObj);

        List<LoginLogDTO> dtos = logs.stream().map(log -> {
            Optional<AppUser> user = userRepo.findByEmail(log.getEmail());
            return loginLogMapper.toDTO(log, user.orElse(null));
        }).toList();

        return new LoginLogListDTO(dtos);
    }
}