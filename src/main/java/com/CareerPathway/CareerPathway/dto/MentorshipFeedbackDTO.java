package com.CareerPathway.CareerPathway.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MentorshipFeedbackDTO {
    @NotNull(message = "Mentorship ID is required")
    private Long mentorshipId;

    @NotBlank(message = "Feedback text is required")
    @Size(max = 1000, message = "Feedback cannot exceed 1000 characters")
    private String feedback;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private Integer rating;
}
