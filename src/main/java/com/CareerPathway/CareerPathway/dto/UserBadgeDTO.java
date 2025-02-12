package com.CareerPathway.CareerPathway.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class UserBadgeDTO {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Badge ID is required")
    private Long badgeId;
}
