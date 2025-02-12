package com.CareerPathway.CareerPathway.model;

import com.CareerPathway.CareerPathway.model.enums.Level;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "trainings")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String title;

    @Size(max = 1000)
    private String description;

    @NotBlank @Size(max = 255)
    private String provider;

    @NotNull @Positive
    private Integer duration; // -> h

    @NotNull @Positive
    private Double cost;

    @NotBlank @Size(max = 50)
        private Level level;

    @ElementCollection
    @CollectionTable(name = "training_skills", joinColumns = @JoinColumn(name = "training_id"))
    private List<String> skillsCovered;
}
