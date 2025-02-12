package com.CareerPathway.CareerPathway.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Table(name = "mentors")
public class Mentor extends User {
    private String expertiseArea;
    private Integer yearsOfExperience;
}
