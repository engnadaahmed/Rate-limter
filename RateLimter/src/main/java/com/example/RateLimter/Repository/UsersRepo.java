package com.example.RateLimter.Repository;

import com.example.RateLimter.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsersRepo extends JpaRepository<Users,Long> {
    // Fetch the configuration ID for a user
    @Query("SELECT u.config FROM Users u WHERE u.id = :userId")
    Long findConfigurationIdByUserId(Long userId);
}
