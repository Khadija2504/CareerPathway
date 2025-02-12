package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.dto.AuthRequest;
import com.CareerPathway.CareerPathway.dto.RegistrationDTO;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.service.UserService;
import com.CareerPathway.CareerPathway.util.PasswordUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final Logger logger = Logger.getLogger(AuthController.class.getName());

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration.time}")
    private long jwtExpirationTime;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationDTO registrationDTO) {
        try {
            User user = userService.registerUser(registrationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        Optional<User> optionalUser = userService.findByEmail(authRequest.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (PasswordUtil.hashPassword(authRequest.getPassword()).equals(user.getPassword())) {
                logger.info("User authenticated successfully: " + user.getEmail() + " and " + user.getId() + user.getRole());

                String token = JWT.create()
                        .withClaim("userId", user.getId())
                        .withClaim("role", String.valueOf(user.getRole()))
                        .sign(Algorithm.HMAC512(jwtSecret));

                return ResponseEntity.ok(Map.of("token", token));
            }
        }
        logger.warning("Unauthorized login attempt for email: " + authRequest.getEmail());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }
}
