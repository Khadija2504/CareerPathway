package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.dto.RegistrationDTO;
import com.CareerPathway.CareerPathway.model.Employee;
import com.CareerPathway.CareerPathway.model.Mentor;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.model.enums.Role;
import com.CareerPathway.CareerPathway.repository.UserRepository;
import com.CareerPathway.CareerPathway.service.UserService;
import com.CareerPathway.CareerPathway.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
private UserRepository userRepository;
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User registerUser(RegistrationDTO registrationDTO) {
        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new RuntimeException("email already registered");
        }

        User user;
        if (registrationDTO.getRole() == Role.EMPLOYEE) {
            user = Employee.builder()
                    .firstName(registrationDTO.getFirstName())
                    .lastName(registrationDTO.getLastName())
                    .email(registrationDTO.getEmail())
                    .password(PasswordUtil.hashPassword(registrationDTO.getPassword()))
                    .role(registrationDTO.getRole())
                    .createdAt(LocalDateTime.now())
                    .department(registrationDTO.getDepartment())
                    .jobTitle(registrationDTO.getJobTitle())
                    .build();
        } else if (registrationDTO.getRole() == Role.MENTOR) {
            user = Mentor.builder()
                    .firstName(registrationDTO.getFirstName())
                    .lastName(registrationDTO.getLastName())
                    .email(registrationDTO.getEmail())
                    .password(PasswordUtil.hashPassword(registrationDTO.getPassword()))
                    .role(registrationDTO.getRole())
                    .role(registrationDTO.getRole())
                    .createdAt(LocalDateTime.now())
                    .expertiseArea(registrationDTO.getExpertiseArea())
                    .yearsOfExperience(registrationDTO.getYearsOfExperience())
                    .build();
        } else {
            throw new RuntimeException("invalid role");
        }

        return userRepository.save(user);
    }

    @Override
    public User updateUserDetails(long id, RegistrationDTO registrationDTO) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();

        user.setFirstName(registrationDTO.getFirstName());
        user.setLastName(registrationDTO.getLastName());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(PasswordUtil.hashPassword(registrationDTO.getPassword()));
        user.setRole(registrationDTO.getRole());

        if (registrationDTO.getRole() == Role.EMPLOYEE) {
            Employee employee = (Employee) user;
            employee.setDepartment(registrationDTO.getDepartment());
            employee.setJobTitle(registrationDTO.getJobTitle());
        } else if (registrationDTO.getRole() == Role.MENTOR) {
            Mentor mentor = (Mentor) user;
            mentor.setExpertiseArea(registrationDTO.getExpertiseArea());
            mentor.setYearsOfExperience(registrationDTO.getYearsOfExperience());
        } else {
            throw new RuntimeException("Invalid role");
        }

        return userRepository.save(user);
    }

    @Override
    public User userDetails(long id) {
        if (userRepository.findById(id).isPresent()) {
            return userRepository.findById(id).orElse(null);
        } else {
            throw new RuntimeException("user not found");
        }
    }
}
