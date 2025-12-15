package app.chatbox.service;

import app.chatbox.dto.LoginLogDTO;
import app.chatbox.dto.LoginLogListDTO;
import app.chatbox.dto.YearlyGraphDTO;
import app.chatbox.mapper.LoginLogMapper;
import app.chatbox.model.AppUser;
import app.chatbox.model.LoginLog;
import app.chatbox.repository.LoginLogRepository;
import app.chatbox.repository.UserRepository;
import app.chatbox.util.Util; // Assuming this is where buildRegexPattern is
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
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
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Sort sortObj = Sort.by(direction, "created_at");

        // Pass statusInput to the repo
        List<LoginLog> logs = loginLogRepo.searchLogs(emailRegex, usernameRegex, statusInput, sortObj);

        List<LoginLogDTO> dtos = logs.stream().map(log -> {
            Optional<AppUser> user = userRepo.findByEmail(log.getEmail());
            return loginLogMapper.toDTO(log, user.orElse(null));
        }).toList();

        return new LoginLogListDTO(dtos);
    }

    public LoginLogListDTO getLogsByEmail(String email) {
        AppUser user = userRepo.findByEmail(email).orElse(null);

        List<LoginLog> logs = loginLogRepo.findByEmail(email, Sort.by(Sort.Direction.DESC, "createdAt"));

        List<LoginLogDTO> dtoList = logs.stream()
                .map(log -> loginLogMapper.toDTO(log, user))
                .toList();

        return new LoginLogListDTO(dtoList);
    }

    public YearlyGraphDTO getActiveUserGraph(Integer year) {
        // 1. Default to current year if null
        int targetYear = (year != null) ? year : Year.now().getValue();

        // 2. Fetch raw data: [Month, Count]
        List<Object[]> rawData = loginLogRepo.countActiveUsersByMonth(targetYear);

        // 3. Initialize list of 12 zeros
        List<Long> monthlyData = new ArrayList<>(Collections.nCopies(12, 0L));

        // 4. Fill in the data
        for (Object[] row : rawData) {
            int month = ((Number) row[0]).intValue();
            long count = ((Number) row[1]).longValue();

            if (month >= 1 && month <= 12) {
                monthlyData.set(month - 1, count);
            }
        }

        return new YearlyGraphDTO(targetYear, monthlyData, "Active Users");
    }
}