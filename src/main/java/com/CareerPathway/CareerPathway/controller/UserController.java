package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.dto.RegistrationDTO;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/details")
    public ResponseEntity<?> userDetails(HttpServletRequest request) {
        int userId = Integer.parseInt(request.getAttribute("userId").toString());
        System.out.println(userId);
        User userDetails = userService.userDetails(userId);
        return ResponseEntity.status(HttpStatus.OK).body(userDetails);
    }

    @GetMapping("/allMentors")
    public ResponseEntity<?> displayAllMentors() {
        List<User> userDetails = userService.allMentors();
        return ResponseEntity.status(HttpStatus.OK).body(userDetails);
    }

    @PutMapping("/updateUserDetails")
    public ResponseEntity<?> updateUserDetails(@RequestBody RegistrationDTO registrationDTO, HttpServletRequest request, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        int userId = Integer.parseInt(request.getAttribute("userId").toString());
        try {
            User user = userService.updateUserDetails(userId, registrationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
