package com.CareerPathway.CareerPathway.dto;

import lombok.Data;

@Data
public class SkillAssessmentDetail {
    private String skillName;
    private int score;
    private int maxScore;
    private double progressPercentage;
}