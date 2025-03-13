package com.CareerPathway.CareerPathway.dto;

import lombok.Data;

@Data
public class CareerPathProgressDetail {
    private String careerPathName;
    private int totalSteps;
    private int completedSteps;
    private double progressPercentage;
}