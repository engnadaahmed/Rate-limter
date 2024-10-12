package com.example.RateLimter.Controller;

import com.example.RateLimter.Entity.Users;
import com.example.RateLimter.Service.Redis_Service.RedisService;
import com.example.RateLimter.Service.user_service.UserService;
import com.example.RateLimter.logging.Logging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UserController {


    @Autowired
    private  UserService userService;

    @Autowired
    private   RedisService redisService;


    @GetMapping("/test/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id ) {

        boolean allowed = redisService.isAllowed(id);
        if (!allowed) {

            return ResponseEntity.status(429).body(HttpStatus.TOO_MANY_REQUESTS);
        }else{

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(userService.getUserById(id));
    }}}
