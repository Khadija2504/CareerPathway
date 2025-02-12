package com.CareerPathway.CareerPathway.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SkillDTO {
    @NotBlank(message = "Skill name is required")
    @Size(max = 255, message = "Skill name cannot exceed 255 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Size(max = 100, message = "Category cannot exceed 100 characters")
    private String category;
}