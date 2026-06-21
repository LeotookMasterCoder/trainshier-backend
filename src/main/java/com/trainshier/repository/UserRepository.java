package com.trainshier.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trainshier.entity.User;

/**
 * User repository.
 */
public interface UserRepository
        extends JpaRepository<User, Long> {

    /**
     * Find user by email.
     *
     * @param email user email
     * @return optional user
     */
    Optional<User> findByEmail(String email);
}