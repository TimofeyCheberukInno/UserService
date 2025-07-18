package com.app.impl.repository;

// Implement CRUD operations:
//Create User/Card
//Get User/Card by id
//Get Users/Cards by ids
//Get User by email
//Update User/Card by id
//Delete User/Card by id

import com.app.impl.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
