package com.CareerPathway.CareerPathway.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class TrainingQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;

    @ElementCollection
    @CollectionTable(name = "quiz_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "quiz_options")
    private List<String> options;

    @NotBlank(message = "Correct answer is required")
    private String correctAnswer;

    @ManyToOne
    @JoinColumn(name = "training_id", nullable = false)
    private Training training;
}
