package com.app.impl.entity;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "card_info",
    indexes = {
        @Index(name = "idx_user_id", columnList = "user_id")
    })
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "user_entity", nullable = false)
    private User user;

    @Column(name = "number", nullable = false, unique = true, length = 30)
    private String cardNumber;

    @Column(name = "holder", nullable = false, length = 100)
    private String cardHolderName;

    @Column(name = "expiration_date", nullable = false)
    LocalDate expirationDate;

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;

        return this.userId.equals(card.userId)
                && this.cardNumber.equals(card.cardNumber)
                && this.cardHolderName.equals(card.cardHolderName)
                && this.expirationDate.equals(card.expirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                userId,
                cardNumber,
                cardHolderName,
                expirationDate
        );
    }
}
