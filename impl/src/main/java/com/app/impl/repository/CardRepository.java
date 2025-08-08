package com.app.impl.repository;

import java.util.Collection;
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
    @Query("SELECT c FROM Card c JOIN FETCH c.user u WHERE c.id = :id")
    Optional<Card> findByIdWithUser(@Param("id") Long id);

    //FIXME
    Optional<Card> findByEmail(@Param("userEmail") String email);

    @Query("SELECT c FROM Card c JOIN FETCH c.user u WHERE u.email = :userEmail")
    Optional<Card> findByEmailWithUser(@Param("userEmail") String userEmail);

    @Modifying
    @Query("UPDATE Card с " +
            "SET с.cardHolderName = :#{#card.cardHolderName} " +
            "WHERE с.id = :#{#card.id}")
    int updateCard(@Param("card") Card card);

    @Query("SELECT c FROM Card c WHERE c.id IN :ids")
    List<Card> findAllByIds(@Param("ids") Collection<Long> ids);

    @Query("SELECT c FROM Card c JOIN FETCH c.user u WHERE c.id IN :ids")
    List<Card> findAllByIdsWithUser(@Param("ids") Collection<Long> ids);

    @Query("SELECT c FROM Card c JOIN FETCH c.user u")
    List<Card> findAllWithUser();
}
