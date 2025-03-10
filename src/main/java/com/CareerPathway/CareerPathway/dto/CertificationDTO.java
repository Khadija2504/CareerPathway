package com.CareerPathway.CareerPathway.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CertificationDTO {
    private Long id;
    private String certificateUrl;
    private LocalDateTime certificationDate;
}
