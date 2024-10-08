package com.example.RateLimter.Repository;

import com.example.RateLimter.Entity.Rate_limit_configurations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateLimitConfigRepo extends JpaRepository<Rate_limit_configurations,Long> {

}
