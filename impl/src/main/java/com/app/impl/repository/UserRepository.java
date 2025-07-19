package com.app.impl.repository;

import com.app.impl.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(@Param("userEmail") String email);

    @Modifying
    @Query("UPDATE User u " +
            "SET u.name = :#{#updateUser.name}, " +
            "u.surname = :#{#updateUser.surname}, " +
            "u.birthDate = :#{#updateUser.birthDate}, " +
            "u.email = :#{#updateUser.email} " +
            "WHERE u.id = :#{#updateUser.id} " +
            "AND NOT EXISTS (SELECT u2 FROM User u2 WHERE u2.email = :#{#updateUser.email} AND u2.id != :#{#updateUser.id})")
    int updateUser(@Param("updateUser") User user);
}
