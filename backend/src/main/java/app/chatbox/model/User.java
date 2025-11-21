package app.chatbox.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "app_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable=false)
    private String password; // lưu hash (bcrypt/argon2)

    private String name;

    private String address;

    private LocalDate birthday;

    private String gender;

    @Column(unique=true, nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean is_active;

    //user / admin
    @Column(nullable = false)
    private String role;

    private boolean is_locked = false;

    private Instant created_at = Instant.now();
    private Instant updated_at = Instant.now();
}
