package app.chatbox.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "group")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 100)
    private String name;

    @Column(length = 20)
    private String encryptionKey;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "last_msg",
            foreignKey = @ForeignKey(name = "fk_last_msg")
    )
    private GroupMsg lastMsg;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();
}
