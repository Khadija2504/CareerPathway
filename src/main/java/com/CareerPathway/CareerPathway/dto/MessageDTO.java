package com.CareerPathway.CareerPathway.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageDTO {
//    private Long senderId;
    @NotNull(message = "receiver ID is required")
    private Long receiverId;

    @NotBlank(message = "Message content is required")
    @Column(columnDefinition = "TEXT")
    private String content;
}
