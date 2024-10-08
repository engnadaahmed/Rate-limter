package com.example.RateLimter.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Rate_limit_configurations {
    @Id
    @Column( nullable = false)
    private Long id;

    @Column(nullable = false)
    private  Integer max_requests;

    @Column(nullable = false)
    private Integer timeWindow; // in seconds //windowLength




}
