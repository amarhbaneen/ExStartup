package com.example.StartupExercise.User;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository interface for User entity.
 * Extends JpaRepository to provide CRUD operations.
 */
public interface UserRepository extends JpaRepository<User,Integer> {
    /**
     * there are no need to implements Load / Create/ Update / Delete functions
     * already exist built-in with Jpa Hibernate
     * if needed custom methods it can be added here
     */

    User findByUsername(String username);
}
