package com.CareerPathway.CareerPathway.model;

import com.CareerPathway.CareerPathway.model.enums.GoalStatus;
import com.CareerPathway.CareerPathway.model.enums.GoalType;
import jakarta.persistence.*;
import java.time.LocalDate;

@Table(name = "employee_goal")
public class EmployeeGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String goalDescription;
    private LocalDate targetDate;
    private GoalStatus status;
    private GoalType type;
}
