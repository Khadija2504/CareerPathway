package com.CareerPathway.CareerPathway.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
public class CareerPath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "careerPath", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CareerPathStep> steps;
}