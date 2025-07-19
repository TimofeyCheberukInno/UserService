package com.app.impl.repository;

import java.util.List;
import com.app.impl.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card,Long> {
    List<Card> findByEmail(@Param("userEmail") String email);

    @Modifying
    @Query("UPDATE Card u " +
            "SET u.user = #{#card.user}, " +
            "u.cardNumber = #{#card.cardNumber}, " +
            "u.cardHolderName = #{#card.cardHolderName}, " +
            "u.expirationDate = #{#card.expirationDate} " +
            "WHERE u.id = #{#card.id}")
    int updateById(@Param("card") Card card);
}
