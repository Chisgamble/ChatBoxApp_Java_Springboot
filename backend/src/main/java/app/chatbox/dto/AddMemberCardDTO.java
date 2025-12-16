package app.chatbox.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddMemberCardDTO {
    private Long userId;
    private String username;
}
