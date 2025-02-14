package com.CareerPathway.CareerPathway.model;

import com.CareerPathway.CareerPathway.model.enums.GoalStatus;
import com.CareerPathway.CareerPathway.model.enums.GoalType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "employee_goal")
public class EmployeeGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;

    private String goalDescription;
    private LocalDate targetDate;
    private GoalStatus status;
    private GoalType type;
}
