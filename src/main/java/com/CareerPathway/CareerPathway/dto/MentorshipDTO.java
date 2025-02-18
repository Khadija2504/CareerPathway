package com.CareerPathway.CareerPathway.dto;

import com.CareerPathway.CareerPathway.model.enums.MentorshipStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MentorshipDTO {
    @NotNull(message = "Mentor ID is required")
    private Long mentorId;

    @Pattern(regexp = "ACTIVE|COMPLETED",
            message = "Status must be ACTIVE or COMPLETED")
    private MentorshipStatus status = MentorshipStatus.Active;
}
