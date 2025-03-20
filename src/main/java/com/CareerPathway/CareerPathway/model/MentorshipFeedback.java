package com.CareerPathway.CareerPathway.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "mentorship_feedbacks")
public class MentorshipFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mentorship_id")
    private Mentorship mentorship;

    @Size(max = 1000)
    private String feedback;

    @Min(1) @Max(5)
    private Integer rating;

    private final LocalDateTime feedbackDate = LocalDateTime.now();
}
