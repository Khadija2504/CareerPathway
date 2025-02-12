package com.CareerPathway.CareerPathway.service;

import com.CareerPathway.CareerPathway.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    Optional<User> findByEmail(String email);
}
