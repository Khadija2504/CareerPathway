package com.CareerPathway.CareerPathway.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
public class QuestionnaireDTO {
    @NotBlank(message = "Question text is required")
    private String questionText;

    private List<String> options;

    @NotBlank(message = "Correct answer is required")
    private String correctAnswer;

    @NotNull(message = "skill id is required")
    private long skillId;
}
