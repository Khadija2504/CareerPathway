package com.CareerPathway.CareerPathway.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "skill_assessments")
public class SkillAssessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Min(0) @Max(100)
    private Integer score;

    private final LocalDateTime assessmentDate = LocalDateTime.now();

    @ElementCollection
    @CollectionTable(name = "strengths", joinColumns = @JoinColumn(name = "assessment_id"))
    @Column(name = "strength")
    private List<String> strengths;

    @ElementCollection
    @CollectionTable(name = "weaknesses", joinColumns = @JoinColumn(name = "assessment_id"))
    @Column(name = "weakness")
    private List<String> weaknesses;

    @ElementCollection
    @CollectionTable(name = "skill_gaps", joinColumns = @JoinColumn(name = "assessment_id"))
    @Column(name = "skill_gap")
    private List<String> skillGaps;
}