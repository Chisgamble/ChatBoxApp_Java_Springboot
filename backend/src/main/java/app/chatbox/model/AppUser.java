package app.chatbox.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

import lombok.*;

import java.time.LocalDate;
import java.time.Instant;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private long id;

    @Column(name="username", length = 50, nullable = false)
    private String username;

    @Column(name="name", length = 50)
    private String name;

    @Column(name="password", length = 100, nullable=false)
    private String password; // lưu hash (bcrypt)

    @Column(name="address", length = 100)
    private String address;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name="gender", length = 10)
    private String gender;

    @Column(name = "email", unique=true, length = 50, nullable = false)
    private String email;

    @Column(name = "is_active")
    private Boolean isActive;

    //user / admin
    @Column(name = "role", nullable = false)
    private String role;

    @Column(name="is_locked")
    private Boolean is_locked = false;

    @Column(name = "created_at",updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();
}
