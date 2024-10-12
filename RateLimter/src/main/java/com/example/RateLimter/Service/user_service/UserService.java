package com.example.RateLimter.Service.user_service;

import com.example.RateLimter.Entity.Users;
import com.example.RateLimter.Repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
   private UsersRepo usersRepo;

    public Optional<Users> getUserById(Long id) {

    return usersRepo.findById(id);

    }
}
