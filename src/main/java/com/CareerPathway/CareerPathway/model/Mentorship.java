package com.CareerPathway.CareerPathway.model;

import com.CareerPathway.CareerPathway.model.enums.MentorshipStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "mentorships")
public class Mentorship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    @ManyToOne
    @JoinColumn(name = "mentee_id")
    private Employee mentee;

    private LocalDateTime startDate = LocalDateTime.now();
    private LocalDateTime endDate;

    private MentorshipStatus status;
}
