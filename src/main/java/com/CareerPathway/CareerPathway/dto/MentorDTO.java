package com.CareerPathway.CareerPathway.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
public class MentorDTO extends UserDTO {
    @NotBlank(message = "Expertise area is required")
    @Size(max = 100, message = "Expertise area cannot exceed 100 characters")
    private String expertiseArea;

    @NotNull(message = "Years of experience is required")
    @Positive(message = "Years of experience must be a positive number")
    private Integer yearsOfExperience;
}