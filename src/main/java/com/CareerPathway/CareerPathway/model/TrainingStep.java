package com.CareerPathway.CareerPathway.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class TrainingStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "training_id", nullable = false)
    @JsonBackReference
    private Training training;

    @OneToMany
    @JoinColumn(name = "course_id", nullable = false)
    @JsonBackReference
    private List<Course> courses;
    private boolean completed;

}
