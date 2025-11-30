package app.chatbox.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "inbox")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Inbox {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userA", nullable = false)
    private AppUser userA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userB", nullable = false)
    private AppUser userB;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "userA_last_seen",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_ib_a_seen")
    )
    private InboxMsg userALastSeen;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "userB_last_seen",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_ib_b_seen")
    )
    private InboxMsg userBLastSeen;

    @Column(updatable = false)
    private Instant created_at = Instant.now();
    private Instant updated_at = Instant.now();
}
