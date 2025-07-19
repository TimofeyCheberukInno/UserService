package com.app.impl.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.CreditCardNumber;

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
@NamedQuery(name = "Card.findByEmail",
        query = "SELECT c FROM Card c WHERE c.user.email = :userEmail")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @CreditCardNumber
    @Column(nullable = false, unique = true)
    private String cardNumber;

    @NotNull
    @Column(nullable = false, length = 100)
    private String cardHolderName;

    @NotNull
    @Column(nullable = false)
    LocalDate expirationDate;
}
