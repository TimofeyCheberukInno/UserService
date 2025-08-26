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
    Optional<User> findByEmail(@Param("email") String email);

    @Modifying
    @Query("UPDATE User u " +
            "SET u.name = :#{#updateUser.name}, " +
            "u.surname = :#{#updateUser.surname}, " +
            "u.email = :#{#updateUser.email} " +
            "WHERE u.id = :#{#updateUser.id} ")
    int updateUser(@Param("updateUser") User user);

    @Query("SELECT u FROM User u WHERE u.id IN :ids")
    List<User> findAllByIds(@Param("ids") Collection<Long> ids);
}
