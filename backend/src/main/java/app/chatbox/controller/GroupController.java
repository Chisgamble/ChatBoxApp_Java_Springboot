package app.chatbox.controller;

import app.chatbox.config.CustomUserDetails;
import app.chatbox.dto.AddMemberCardDTO;
import app.chatbox.dto.GroupCardDTO;
import app.chatbox.dto.GroupMemberDTO;
import app.chatbox.dto.request.*;
import app.chatbox.dto.response.GeneralResDTO;
import app.chatbox.dto.response.GroupUserResDTO;
import app.chatbox.services.GroupMemberService;
import app.chatbox.services.GroupMsgService;
import app.chatbox.services.GroupService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService service;
    private final GroupMemberService groupMemberService;
    private final GroupMsgService groupMsgService;

    @PreAuthorize("isAuthenticated() or hasRole('ADMIN')")
    @GetMapping("/{id}/messages")
    public ResponseEntity<GroupUserResDTO> getGroupInfoAndMsgs(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(service.getUserGroupInfo(id, user.getId()));
    }

    @PreAuthorize("isAuthenticated() or hasRole('ADMIN')")
    @DeleteMapping("{id}/messages")
    public ResponseEntity<GeneralResDTO> deleteGroupMessageBySender(
            @PathVariable Long id,
            @RequestBody DeleteMsgsBySenderReq req,
            @AuthenticationPrincipal CustomUserDetails user) {
        groupMsgService.deleteMessageBySender(id, req.getMsgIds(), user.getId());
        return ResponseEntity.ok(new GeneralResDTO("Delete group messages succeed"));
    }


    @PreAuthorize("isAuthenticated() or hasRole('ADMIN')")
    @DeleteMapping("/{id}/messages/all")
    public ResponseEntity<GeneralResDTO> deleteAllMessages(@PathVariable Long id) {
        service.deleteAllMessages(id);
        return ResponseEntity.ok(
                new GeneralResDTO("All inbox messages removed successfully"));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/leave")
    public ResponseEntity<GeneralResDTO> leaveGroup(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        service.leaveGroup(id, user.getId());
        return ResponseEntity.ok(
                new GeneralResDTO("Left group successfully")
        );
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResDTO> deleteGroup(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        service.deleteGroup(id, user.getId());
        return ResponseEntity.ok(new GeneralResDTO("Group deleted successfully"));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<GroupCardDTO> createGroup(
            @RequestBody CreateGroupReqDTO req,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(service.createGroup(req, user.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/available-friends")
    public ResponseEntity<List<AddMemberCardDTO>> getAllFriendsNotInGroup(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(service.getAllFriendsNotInGroup(id, user.getId()));
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/member")
    public ResponseEntity<GeneralResDTO> addMember(
            @PathVariable Long id,
            @RequestBody AddGroupMemberReqDTO req,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        service.addMember(id, user.getId(), req.getUserId());
        return ResponseEntity.ok(new GeneralResDTO("Member added"));
    }

    @PostMapping("/{groupId}/members")
    public ResponseEntity<?> addMembers(
            @PathVariable Long groupId,
            @RequestBody AddMembersReqDTO req,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        service.addMembers(groupId, user.getId(), req.getUserIds());
        return ResponseEntity.ok(new GeneralResDTO("Added new members"));
    }


    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<GeneralResDTO> removeMember(
            @PathVariable Long id,
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails admin
    ) {
        service.removeMember(id, admin.getId(), userId);
        return ResponseEntity.ok(new GeneralResDTO("Member removed"));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{groupId}/members/{userId}/promote")
    public ResponseEntity<GeneralResDTO> promoteMember(
            @PathVariable Long groupId,
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        groupMemberService.promoteToAdmin(groupId, userId, user.getId());
        return ResponseEntity.ok(
                new GeneralResDTO("Promoted to admin successfully")
        );
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/name")
    public ResponseEntity<GroupCardDTO> changeGroupName(
            @PathVariable Long id,
            @RequestBody ChangeGroupNameReqDTO req,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(
                service.changeGroupName(id, user.getId(), req.getNewName())
        );
    }

    @PreAuthorize("isAuthenticated() or hasRole('ADMIN')")
    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<GroupMemberDTO>> getGroupMembers(
            @PathVariable Long groupId
    ) {
        return ResponseEntity.ok(groupMemberService.getAllMembersDTO(groupId));
    }

}
