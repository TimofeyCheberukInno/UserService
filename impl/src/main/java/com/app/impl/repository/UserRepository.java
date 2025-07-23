package com.app.impl.repository;

import java.util.Collection;
import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.impl.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(@Param("userEmail") String email);

    @Modifying
    @Query("UPDATE User u " +
            "SET u.name = :#{#updateUser.name}, " +
            "u.surname = :#{#updateUser.surname}, " +
            "u.birthDate = :#{#updateUser.birthDate}, " +
            "u.email = :#{#updateUser.email} " +
            "WHERE u.id = :#{#updateUser.id} ")
    Optional<User> updateUser(@Param("updateUser") User user);

    List<User> findAllById(Collection<Long> ids);
}
