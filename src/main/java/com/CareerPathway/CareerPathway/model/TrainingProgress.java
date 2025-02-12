package com.CareerPathway.CareerPathway.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainingProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Training training;

    private int progressPercentage;
    private boolean completed;

    private LocalDateTime startDate;
    private LocalDateTime completionDate;
}
