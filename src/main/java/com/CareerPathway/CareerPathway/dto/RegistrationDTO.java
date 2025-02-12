package com.CareerPathway.CareerPathway.dto;

import com.CareerPathway.CareerPathway.model.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegistrationDTO {
    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name cannot exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name cannot exceed 100 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotNull(message = "Role is required")
    private Role role;

    // for employee
    private String department;
    private String jobTitle;

    // for mentor
    private String expertiseArea;
    private Integer yearsOfExperience;
}