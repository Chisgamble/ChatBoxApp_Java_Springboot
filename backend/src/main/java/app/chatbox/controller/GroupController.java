package app.chatbox.controller;

import app.chatbox.config.CustomUserDetails;
import app.chatbox.dto.response.GroupUserResDTO;
import app.chatbox.services.GroupService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService service;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/messages")
    public ResponseEntity<GroupUserResDTO> getGroupInfoAndMsgs(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(service.getUserGroupInfo(id, user.getId()));
    }
}
