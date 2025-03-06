package com.CareerPathway.CareerPathway.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
public class CareerPath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "careerPath", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CareerPathStep> steps;

    public void setSteps(List<CareerPathStep> steps) {
        this.steps = steps;
        for (CareerPathStep step : steps) {
            step.setCareerPath(this);
        }
    }
}