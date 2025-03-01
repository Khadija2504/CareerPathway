package com.CareerPathway.CareerPathway.dto;

import com.CareerPathway.CareerPathway.model.enums.ResourceType;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class ResourceDTO {
    @NotBlank(message = "Resource title is required")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @NotBlank(message = "Resource type is required")
    @Pattern(regexp = "EBOOK|ARTICLE|CASE_STUDY|VIDEO",
            message = "Type must be EBOOK, ARTICLE, CASE_STUDY, or VIDEO")
    private ResourceType type;

    @NotBlank(message = "Resource URL is required")
    @Size(max = 255, message = "URL cannot exceed 255 characters")
    private String url;

    @Size(max = 100, message = "Category cannot exceed 100 characters")
    private String category;
    @NotBlank(message = "Resource image is required")
    private String image;
}
