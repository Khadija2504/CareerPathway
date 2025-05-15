package com.CareerPathway.CareerPathway.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingStepDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;

    @NotNull(message = "Training ID is required")
    private Long trainingId;

    @NotEmpty(message = "At least one course ID is required")
    private List<@NotNull(message = "Course ID cannot be null") Long> courseIds;

    private List<@NotNull(message = "Resource ID cannot be null") Long> resourceIds;

    private boolean completed = false;
}
