package com.example.RateLimter.Service.Redis_Service;

import com.example.RateLimter.Entity.Rate_limit_configurations;
import com.example.RateLimter.Repository.RateLimitConfigRepo;
import com.example.RateLimter.Repository.UsersRepo;

import com.example.RateLimter.logging.Logging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RedisService {
    @Autowired
    private RateLimitConfigRepo configRepository;
    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private Logging logg;



    private static final String REDIS_KEY_PREFIX = "rate_limit:";
    // Time-to-Live (TTL) for Redis cache, set to 10 minutes (600 seconds)
    private static final long TTL = 600L;


    /**
     * Get configuration for a user. If not found in Redis cache, fetch from database,
     * then cache it in Redis with a TTL.
     */
    public Rate_limit_configurations getConfiguration(Long configurationId) {
        String redisConfigKey = "config:" + configurationId;
       logg.writeToFile("Fetching configuration for ID:" + configurationId);

        // Try to get configuration from Redis cache
        Rate_limit_configurations config = (Rate_limit_configurations) redisTemplate.opsForValue().get(redisConfigKey);

        if (config == null) {

            // If not found in cache, fetch from database
            config = configRepository.findById(configurationId)
                    .orElseThrow(() -> new RuntimeException("Configuration not found"));

            // Cache the configuration in Redis with a TTL of 10 minutes
            redisTemplate.opsForValue().set(redisConfigKey, config, TTL, TimeUnit.SECONDS);
            logg.writeToFile("Configuration cached in Redis for ID: {}" + configurationId);
        }

        return config;
    }

    public boolean isAllowed(Long userId) {//sliding window algorithm

        // Fetch configuration_id for the user from Postgres
        Long configurationId = usersRepo.findConfigurationIdByUserId(userId);
        if (configurationId == null) {
            logg.writeToFile("Configuration ID not found for user ID:" + userId);
            throw new RuntimeException("Configuration not found for user ID: " + userId);
        }



        Rate_limit_configurations config=getConfiguration(configurationId);
        logg.writeToFile("Rate limit configuration for user ID: "+ config +userId);
        String redisKey = REDIS_KEY_PREFIX + userId.toString();
        long currentTime = System.currentTimeMillis();

        // Get all request timestamps within the time window from Redis
        List<Object> timestamps = redisTemplate.opsForList().range(redisKey, 0, -1);
        long windowStart = currentTime - (config.getTimeWindow() * 1000);

        // Remove timestamps older than the time window
        List<Long> validTimestamps = timestamps.stream()
                .map(obj -> (Long) obj) // Cast each Object to Long
                .filter(timestamp -> timestamp >= windowStart)
                .collect(Collectors.toList());
        logg.writeToFile ("Valid timestamps for user ID " + userId + ": " + validTimestamps.size());
        // If within rate limit, add current request timestamp
        if (validTimestamps.size() < config.getMax_requests()) {
            redisTemplate.opsForList().rightPush(redisKey, currentTime);
            redisTemplate.expire(redisKey, config.getTimeWindow(), TimeUnit.SECONDS);
            return true;
        }
        logg.writeToFile("Rate limit exceeded. Try later.");

        return false; // Not allowed

    }


}
