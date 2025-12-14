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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userA", nullable = false)
    private AppUser userA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userB", nullable = false)
    private AppUser userB;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "userA_last_seen",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_ib_a_seen")
    )
    private InboxMsg userALastSeen;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "userB_last_seen",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_ib_b_seen")
    )
    private InboxMsg userBLastSeen;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "last_msg",
        foreignKey = @ForeignKey(name = "fk_ib_last_msg")
    )
    private InboxMsg lastMsg;

    @Column(name = "created_at",updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();
}
