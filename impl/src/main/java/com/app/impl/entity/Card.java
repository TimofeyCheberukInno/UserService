package com.app.impl.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "card_info",
    indexes = {
        @Index(name = "idx_user_id", columnList = "user_id")
    })
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @Column(nullable = false, unique = true)
    @Size(min = 8, max = 8)
    private String cardNumber;

    @Column(nullable = false, length = 100)
    private String cardHolderName;

    @Column(nullable = false)
    LocalDate expirationDate;
}
