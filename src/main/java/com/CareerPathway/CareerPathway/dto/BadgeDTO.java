package com.CareerPathway.CareerPathway.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class BadgeDTO {
    @NotBlank(message = "Badge name is required")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotBlank(message = "Criteria is required")
    @Size(max = 1000, message = "Criteria cannot exceed 1000 characters")
    private String criteria;
}
