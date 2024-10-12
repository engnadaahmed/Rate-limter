package com.example.RateLimter.Repository;

import com.example.RateLimter.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsersRepo extends JpaRepository<Users,Long> {
    // Fetch the configuration ID for a user
    @Query("SELECT u.config.id FROM Users u WHERE u.id = :userId")
    Long findConfigurationIdByUserId(@Param("userId")Long userId);
}

