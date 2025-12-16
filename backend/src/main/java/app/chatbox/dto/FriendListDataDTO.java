package app.chatbox.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendListDataDTO {
    private String username;
    private long friendCount;
    private long friendOfFriendCount;
    private String createdAt;
}