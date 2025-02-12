package com.CareerPathway.CareerPathway.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Table(name = "employees")
public class Employee extends User {
    private String department;
    private String jobTitle;
}
