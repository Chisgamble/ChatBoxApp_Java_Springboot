package app.chatbox.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "group_member")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "group_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_groupmember_group")
    )
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_groupmember_user")
    )
    private AppUser user;

    @Column(length = 10)
    private String role;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "last_seen_msg",
            foreignKey = @ForeignKey(name = "fk_groupmember_last_seen_msg")
    )
    private GroupMsg lastSeenMsg;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();
}
