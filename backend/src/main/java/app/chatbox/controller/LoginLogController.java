package app.chatbox.controller;

import app.chatbox.dto.LoginLogListDTO;
import app.chatbox.dto.YearlyGraphDTO;
import app.chatbox.services.LoginLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/login-log")
@RequiredArgsConstructor
public class LoginLogController {

    private final LoginLogService loginLogService;

    @GetMapping("/getall")
    public ResponseEntity<LoginLogListDTO> getAll(
            @RequestParam(required = false) List<String> email,
            @RequestParam(required = false) List<String> username,
            @RequestParam(defaultValue = "all") String status,
            @RequestParam(defaultValue = "asc") String order
    ) {
        return ResponseEntity.ok(
                loginLogService.getAllLogsAndData(email, username, status, order)
        );
    }

    @GetMapping("/find-by-email")
    public ResponseEntity<LoginLogListDTO> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok((loginLogService.getLogsByEmail(email)));
    }

    @GetMapping("/graph")
    public ResponseEntity<YearlyGraphDTO> getActiveUserGraph(
            @RequestParam(required = false) Integer year
    ) {
        return ResponseEntity.ok(loginLogService.getActiveUserGraph(year));
    }
}