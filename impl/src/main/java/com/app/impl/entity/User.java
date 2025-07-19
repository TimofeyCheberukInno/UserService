package com.app.impl.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@NamedQuery(
        name = "User.findByEmail",
        query = "SELECT u FROM User u WHERE u.email = :userEmail"
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, length = 50)
    private String name;

    @NotNull
    @Column(nullable = false, length = 50)
    private String surname;

    @NotNull
    @PastOrPresent
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @NotNull
    @Email
    @Column(nullable = false, unique = true)
    private String email;
}



