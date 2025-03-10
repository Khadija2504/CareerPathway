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

    @OneToOne(mappedBy = "careerPath", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Certification certification;

    private boolean done;

    public void setSteps(List<CareerPathStep> steps) {
        this.steps = steps;
        for (CareerPathStep step : steps) {
            step.setCareerPath(this);
        }
    }

    @Override
    public String toString() {
        return "CareerPath{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", employee=" + (employee != null ? employee.getId() : null) +
                ", steps=" + (steps != null ? steps.size() : 0) +
                ", done=" + done +
                '}';
    }
}