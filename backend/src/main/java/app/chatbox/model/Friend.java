package app.chatbox.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(
    name = "friend",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_friend_req_pair",
            columnNames = {"userA", "userB"}
        )
    }
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "usera",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_friend_usera")
    )
    private AppUser userA;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "userb",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_friend_userb")
    )
    private AppUser userB;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "last_msg",
        foreignKey = @ForeignKey(name = "fk_friend_last_msg")
    )
    private InboxMsg lastMsg;

    @Column(name = "created_at",updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();
}
