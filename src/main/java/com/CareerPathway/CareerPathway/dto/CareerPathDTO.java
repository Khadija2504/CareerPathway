package com.CareerPathway.CareerPathway.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CareerPathDTO {
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
}
