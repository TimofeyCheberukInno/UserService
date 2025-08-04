package com.app.impl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.impl.entity.Card;

@Repository
public interface CardRepository extends JpaRepository<Card,Long> {
    Optional<Card> findByEmail(@Param("userEmail") String email);

    @Query("SELECT c FROM Card c JOIN FETCH c.user u WHERE u.email = :userEmail")
    Optional<Card> findByEmailWithUser(String userEmail);

    @Modifying
    @Query("UPDATE Card с " +
            "SET с.user = :#{#card.user}, " +
            "с.cardNumber = :#{#card.cardNumber}, " +
            "с.cardHolderName = :#{#card.cardHolderName}, " +
            "с.expirationDate = :#{#card.expirationDate} " +
            "WHERE с.id = :#{#card.id}")
    int updateById(@Param("card") Card card);

    @Query("SELECT c FROM Card c JOIN FETCH c.user u")
    List<Card> findAllWithUser();

    @Query("SELECT c FROM Card c JOIN FETCH c.user u WHERE c.id = :id")
    Optional<Card> findByIdWithUser(Long id);
}
