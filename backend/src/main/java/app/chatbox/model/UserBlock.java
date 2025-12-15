package app.chatbox.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(
        name = "user_block",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_block_pair",
                        columnNames = {"blocker_id", "blocked_id"}
                )
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class UserBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "blocker_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_userblock_blocker")
    )
    private AppUser blocker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "blocked_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_userblock_blocked")
    )
    private AppUser blocked;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();
}
