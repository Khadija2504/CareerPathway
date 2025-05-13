package com.CareerPathway.CareerPathway.model;

import com.CareerPathway.CareerPathway.model.enums.Level;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
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

    @NotNull
    private Integer duration; // -> h

    @NotNull
    private Level level;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = true)
    private User mentor;

    @ElementCollection
    @CollectionTable(name = "training_skills", joinColumns = @JoinColumn(name = "training_id"))
    private List<String> skillsCovered;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    private boolean completed;
    private Integer score;
}
