package com.CareerPathway.CareerPathway.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
public class EmployeeGoalDTO {
    @NotBlank(message = "Goal description is required")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String goalDescription;

    @NotNull(message = "Target date is required")
    @Future(message = "Target date must be in the future")
    private LocalDate targetDate;

    @Pattern(regexp = "NOT_STARTED|IN_PROGRESS|COMPLETED",
            message = "Status must be NOT_STARTED, IN_PROGRESS, or COMPLETED")
    private String status = "NOT_STARTED";
}
