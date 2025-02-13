package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
