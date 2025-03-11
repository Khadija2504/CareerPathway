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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> registerUser(@RequestPart("user") RegistrationDTO registrationDTO,
                                          @RequestPart("file") MultipartFile file,
                                          BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            String fileName = file.getOriginalFilename();
            String filePath = Paths.get("uploads", fileName).toString();
            Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

            registrationDTO.setImgUrl("http://localhost:8800/uploads/" + fileName);

            User user = userService.registerUser(registrationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
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
