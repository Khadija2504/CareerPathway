package com.CareerPathway.CareerPathway.service;

import com.CareerPathway.CareerPathway.dto.RegistrationDTO;
import com.CareerPathway.CareerPathway.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    Optional<User> findByEmail(String email);
    Optional<User> findById(long id);
    User registerUser(RegistrationDTO user);
    User userDetails(long id);
    User updateUserDetails(long id, RegistrationDTO user);
    List<User> allMentors();
    List<User> allEmployees();
}
