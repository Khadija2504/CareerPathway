package com.CareerPathway.CareerPathway.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "career_path_steps")
public class CareerPathStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(max = 255)
    private String title;

    @Size(max = 500)
    private String description;

    @ManyToOne
    @JoinColumn(name = "career_path_id")
    private CareerPath careerPath;

    @ManyToOne
    @JoinColumn(name = "required_skill_id")
    private Skill requiredSkill;
}
