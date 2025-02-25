package com.CareerPathway.CareerPathway.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.*;

@Data
public class CourseDTO {

    @NotNull(message = "Title cannot be null")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @NotNull(message = "Description cannot be null")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    private String description;

    @NotNull(message = "Type cannot be null")
    @Pattern(regexp = "^(Online|Offline|Hybrid)$", message = "Type must be Online, Offline, or Hybrid")
    private String type;

    @NotNull(message = "Category cannot be null")
    @Pattern(regexp = "^(Education|Technology|Health|Other)$", message = "Category must be Education, Technology, Health, or Other")
    private String category;

    @NotNull(message = "URL cannot be null")
    @URL(message = "Invalid URL format")
    private String url;
}
