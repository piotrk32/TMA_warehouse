package com.example.tma_warehouse.repositories;

import com.example.tma_warehouse.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);


    @Query("""
            SELECT u.email
            FROM User u
            WHERE u.id = :userId
            """)
    Optional<String> findUserEmailByUserId(@Param("userId") Long userId);

    Optional<User> findUserByEmail(String email);

    @Query("""
            SELECT u.idToken
            FROM User u
            WHERE u.email = :email
            """)
    String findIdTokenByEmail(@Param("email") String email);

    @Query("""
            SELECT u.refreshToken
            FROM User u
            WHERE u.email = :email
            """)
    String findRefreshTokenByEmail(@Param("email") String email);

    @Query("""
            SELECT u.accessToken
            FROM User u
            WHERE u.email = :email
            """)
    String findAccessTokenByEmail(@Param("email") String email);

    @Query("""
            SELECT u.id
            FROM User u
            WHERE u.email = :email
            """)
    Optional<Long> getUserIdByUserEmail(@Param("email") String email);


}