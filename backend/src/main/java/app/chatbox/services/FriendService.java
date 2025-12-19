package app.chatbox.services;

import app.chatbox.dto.FriendCardDTO;
//import app.chatbox.mapper.FriendMapper;
import app.chatbox.dto.FriendListDataDTO;
import app.chatbox.model.Friend;
import app.chatbox.repository.FriendRepository;
import app.chatbox.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {
    private final FriendRepository friendRepository;
//    private final FriendMapper friendMapper;

    public void deleteFriend(Long friendId, Long userId) {
        Long a = Math.min(friendId, userId);
        Long b = Math.max(friendId, userId);

        Friend friend = friendRepository
                .findByUserA_IdAndUserB_Id(a, b)
                .orElseThrow(() ->
                        new RuntimeException("Friend not found"));

        friendRepository.delete(friend);
    }

    public List<FriendCardDTO> getAllUserFriends(Long id){
        return friendRepository.getFriendCards(id);
    }

    public List<FriendListDataDTO> getFriendListData(
            List<String> username,
            String sortBy,
            String sortDir,
            String fcSymbol,
            Integer fcVal
    ) {
        String usernameRegex = Util.buildRegexPattern(username);
        String finalSortBy = (sortBy == null || sortBy.isEmpty()) ? "username" : sortBy;
        String finalSortDir = (sortDir == null || sortDir.isEmpty()) ? "asc" : sortDir;

        List<Object[]> results = friendRepository.findFriendListDataRaw(
                usernameRegex,
                fcSymbol,
                fcVal,
                finalSortBy,
                finalSortDir
        );

        List<FriendListDataDTO> dtos = new ArrayList<>();
        for (Object[] row : results) {
            Timestamp timestamp = (Timestamp) row[1];
            String createdAtString = null;

            if (timestamp != null) {
                // 1. Convert Timestamp to LocalDateTime (implicitly done via Timestamp.toLocalDateTime() in Java 8+)
                createdAtString = timestamp.toLocalDateTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); // Format LocalDateTime without zone
            }

            dtos.add(FriendListDataDTO.builder()
                    .username((String) row[0])
                    .createdAt(createdAtString) // Assign the formatted String
                    .friendCount(((Number) row[2]).longValue())
                    .friendOfFriendCount(((Number) row[3]).longValue())
                    .build());
        }

        return dtos;
    }
}
