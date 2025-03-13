package com.CareerPathway.CareerPathway.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProgressMetricsDTO {
    private double skillAssessmentProgress;
    private double careerPathProgress;
    private double trainingProgress;
    private double goalProgress;

    private List<CareerPathProgressDetail> careerPathProgressDetails;

    private List<SkillAssessmentDetail> skillAssessmentDetails;

}