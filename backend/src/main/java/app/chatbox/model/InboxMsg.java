package app.chatbox.model;

import jakarta.persistence.*;

import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "inbox_message")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InboxMsg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inbox_id", nullable = false)
    private Inbox inbox;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private AppUser sender;

    //sending (loading) / sent / seen
    @Column(length = 30)
    private String status;

    @Column(length = 500)
    private String content;

    @Column(name = "created_at",updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();
}