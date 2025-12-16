package app.chatbox.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(
        name = "spam_report",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_report_pair",
                        columnNames = {"reporter_id", "reported_id"}
                )
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class SpamReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "reporter_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_spamreport_reporter")
    )
    private AppUser reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "reported_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_spamreport_reported")
    )
    private AppUser reported;

    // Added Status Field
    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "created_at", updatable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    @Builder.Default
    private Instant updatedAt = Instant.now();

    // Lifecycle hook to update timestamp automatically
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }
}