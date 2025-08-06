package com.shubham.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Getter
@Setter
@Entity
@Table(name = "users", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_gen")
    @SequenceGenerator(name = "users_id_gen", sequenceName = "users_user_id_seq", allocationSize = 1)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Size(max = 50)
    @Column(name = "google_id", length = 50)
    private String googleId;

    @Size(max = 50)
    @NotNull
    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Size(max = 100)
    @NotNull
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Size(max = 255)
    @Column(name = "password")
    private String password;

    @Size(max = 20)
    @Column(name = "phone_no", length = 20)
    private String phoneNo;

    @Size(max = 100)
    @NotNull
    @Column(name = "hostel_name", nullable = false, length = 100)
    private String hostelName;

    @Size(max = 10)
    @NotNull
    @Column(name = "room_number", nullable = false, length = 10)
    private String roomNumber;

    @Size(max = 20)
    @ColumnDefault("'USER'")
    @Column(name = "role", length = 20)
    private String role;

    @Size(max = 100)
    @ColumnDefault("'Unknown'")
    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "profile_image", length = Integer.MAX_VALUE)
    private String profileImage;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @OneToMany(mappedBy = "user")
    private Set<UserProduct> userProducts = new LinkedHashSet<>();

}
