package com.example.RateLimter.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Users {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String name;

    private  String email;


    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "Rate_limit_configurations_id")
    private Rate_limit_configurations config;

}
