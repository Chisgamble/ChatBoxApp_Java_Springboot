package app.chatbox.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "inbox")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inbox {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long userA;
    private long userB;

    private long userA_last_seen;
    private long userB_last_seen;

    private Instant created_at = Instant.now();
    private Instant updated_at = Instant.now();
}
