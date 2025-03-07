package com.CareerPathway.CareerPathway.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CareerPathDTO {
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    private boolean done = false;

    private List<CareerPathStepDTO> steps;

}
