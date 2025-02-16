package com.CareerPathway.CareerPathway.dto;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
public class GoalUpdateDTO {
    private Long goalId;
    @Pattern(regexp = "IN_PROGRESS|COMPLETED",
            message = "Status must be IN_PROGRESS, or COMPLETED")
    private String status;
}
