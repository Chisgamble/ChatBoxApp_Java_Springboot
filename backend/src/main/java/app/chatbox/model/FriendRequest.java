package app.chatbox.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(
    name = "friend_request",
        uniqueConstraints = {
            @UniqueConstraint(
                name = "uq_friend_req_pair",
                columnNames = {"senderID", "receiverID"}
            )
    }
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "sender_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_friendreq_sender")
    )
    private AppUser sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "receiver_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_friendreq_receiver")
    )
    private AppUser receiver;

    @Column(length = 10)
    private String status;

    @Column(name = "created_at",updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();
}
