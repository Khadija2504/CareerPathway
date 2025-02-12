package com.CareerPathway.CareerPathway.dto;

import com.CareerPathway.CareerPathway.model.enums.Level;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
public class TrainingDTO {
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotBlank(message = "Provider is required")
    @Size(max = 255, message = "Provider name cannot exceed 255 characters")
    private String provider;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be a positive number")
    private Integer duration;

    @NotNull(message = "Cost is required")
    @Positive(message = "Cost must be a positive number")
    private Double cost;

    @NotBlank(message = "Level is required")
    @Pattern(regexp = "BEGINNER|INTERMEDIATE|ADVANCED",
            message = "Level must be BEGINNER, INTERMEDIATE, or ADVANCED")
    private Level level;

    private List<String> skillsCovered;
}
