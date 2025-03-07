package com.CareerPathway.CareerPathway.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CertificationDTO {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Career path ID is required")
    private Long careerPathId;

    @NotBlank(message = "Certificate URL is required")
    @Size(max = 255, message = "URL cannot exceed 255 characters")
    private String certificateUrl;
}
