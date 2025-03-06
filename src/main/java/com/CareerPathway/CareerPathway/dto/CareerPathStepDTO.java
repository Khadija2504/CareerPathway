package com.CareerPathway.CareerPathway.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class CareerPathStepDTO {
    @NotBlank(message = "Step title is required")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Career path ID is required")
    private Long careerPathId;

    private boolean done = false;

    @NotNull(message = "Required skill ID is required")
    private Long requiredSkillId;
}
