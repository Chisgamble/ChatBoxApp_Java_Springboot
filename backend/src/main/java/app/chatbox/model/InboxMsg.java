package app.chatbox.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.Instant;

@Entity
@Table(name = "inbox_message")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InboxMsg {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inboxID", nullable = false)
    private Inbox inbox;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senderID", nullable = false)
    private AppUser sender;

    //sending (loading) / sent / seen
    @Column(length = 30)
    private String status;

    @Column(length = 500)
    private String content;

    @Column(name = "created_at", updatable = false)
    private Instant created_at = Instant.now();
    private Instant updated_at = Instant.now();
}