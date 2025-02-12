package com.CareerPathway.CareerPathway.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@Table(name = "mentors")
public class Mentor extends User {
    private String expertiseArea;
    private Integer yearsOfExperience;
}
