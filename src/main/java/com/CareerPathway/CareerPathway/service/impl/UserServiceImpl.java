package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.repository.UserRepository;
import com.CareerPathway.CareerPathway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
