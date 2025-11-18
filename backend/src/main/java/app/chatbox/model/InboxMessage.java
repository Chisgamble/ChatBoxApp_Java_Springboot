package app.chatbox.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.Instant;

@Entity
@Table(name = "inbox_message")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InboxMessage{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inboxID", nullable = false)
    private Inbox inbox;

    @Column(name = "senderID", nullable = false)
    private int senderID;

    //sending (loading) / sent / seen
    private String status;

    private String content;

    private Instant created_at = Instant.now();
    private Instant updated_at = Instant.now();
}