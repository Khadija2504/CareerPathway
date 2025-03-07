package com.CareerPathway.CareerPathway.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Long requiredSkillId;

    private boolean done;

    @ManyToOne
    @JoinColumn(name = "career_path_id", nullable = false)
    @JsonBackReference
    private CareerPath careerPath;

    @Override
    public String toString() {
        return "CareerPathStep{" +
                "id=" + id +
                ", name='" + title + '\'' +
                ", done=" + done +
                ", careerPath=" + (careerPath != null ? careerPath.getId() : null) +
                '}';
    }
}