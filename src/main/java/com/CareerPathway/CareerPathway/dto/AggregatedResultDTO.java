package com.CareerPathway.CareerPathway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggregatedResultDTO {
    private Long employeeId;
    private String employeeName;
    private int skillAssessmentPercentage;
    private int careerPathProgressPercentage;
    private int trainingProgramsCount;
    private int incompleteGoalsCount;
}