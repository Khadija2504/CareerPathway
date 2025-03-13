package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.dto.*;
import com.CareerPathway.CareerPathway.model.*;
import com.CareerPathway.CareerPathway.model.enums.GoalStatus;
import com.CareerPathway.CareerPathway.repository.*;
import com.CareerPathway.CareerPathway.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressServiceImpl implements ProgressService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SkillAssessmentRepository skillAssessmentRepository;
    @Autowired
    private CareerPathRepository careerPathRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private GoalRepository employeeGoalRepository;

    @Override
    public ProgressMetricsDTO calculateProgressMetrics(Long userId) {
        ProgressMetricsDTO metrics = new ProgressMetricsDTO();
        User employee = userRepository.findById(userId).get();

        List<SkillAssessment> assessments = skillAssessmentRepository.findByUserId(userId);
        metrics.setSkillAssessmentProgress(calculateSkillAssessmentProgress(assessments));
        metrics.setSkillAssessmentDetails(getSkillAssessmentDetails(assessments));

        List<CareerPath> careerPaths = careerPathRepository.findCareerPathByEmployee(employee);
        metrics.setCareerPathProgress(calculateCareerPathProgress(careerPaths));
        metrics.setCareerPathProgressDetails(getCareerPathProgressDetails(careerPaths));

        List<Training> trainings = trainingRepository.findByUser(employee);
        metrics.setTrainingProgress(calculateTrainingProgress(trainings));

        List<EmployeeGoal> goals = employeeGoalRepository.findEmployeeGoalByEmployeeId(userId);
        metrics.setGoalProgress(calculateGoalProgress(goals));

        return metrics;
    }

    private double calculateSkillAssessmentProgress(List<SkillAssessment> assessments) {
        if (assessments.isEmpty()) return 0;
        return assessments.stream().mapToInt(SkillAssessment::getScore).average().orElse(0);
    }

    private double calculateCareerPathProgress(List<CareerPath> careerPaths) {
        if (careerPaths.isEmpty()) return 0;
        int totalSteps = careerPaths.stream().mapToInt(CareerPath::getTotalSteps).sum();
        int completedSteps = careerPaths.stream().mapToInt(CareerPath::getCompletedSteps).sum();
        return (totalSteps == 0) ? 0 : (completedSteps * 100.0) / totalSteps;
    }

    private double calculateTrainingProgress(List<Training> trainings) {
        if (trainings.isEmpty()) return 0;
        return (trainings.size() == 0) ? 0 : (trainings.size() * 100.0) / trainings.size();
    }

    private double calculateGoalProgress(List<EmployeeGoal> goals) {
        if (goals.isEmpty()) return 0;
        long completedGoals = goals.stream().filter(g -> GoalStatus.COMPLETED.equals(g.getStatus())).count();
        return (goals.size() == 0) ? 0 : (completedGoals * 100.0) / goals.size();
    }

    private List<SkillAssessmentDetail> getSkillAssessmentDetails(List<SkillAssessment> assessments) {
        Map<Skill, List<SkillAssessment>> assessmentsBySkill = assessments.stream()
                .collect(Collectors.groupingBy(SkillAssessment::getSkill));

        return assessmentsBySkill.entrySet().stream()
                .map(entry -> {
                    Skill skill = entry.getKey();
                    List<SkillAssessment> skillAssessments = entry.getValue();

                    int totalScore = skillAssessments.stream().mapToInt(SkillAssessment::getScore).sum();
                    int maxScore = skillAssessments.size() * 100;

                    double progressPercentage = (maxScore == 0) ? 0 : (totalScore * 100.0) / maxScore;

                    SkillAssessmentDetail detail = new SkillAssessmentDetail();
                    detail.setSkillName(skill.getName());
                    detail.setScore(totalScore);
                    detail.setMaxScore(maxScore);
                    detail.setProgressPercentage(progressPercentage);
                    return detail;
                })
                .collect(Collectors.toList());
    }

    private List<CareerPathProgressDetail> getCareerPathProgressDetails(List<CareerPath> careerPaths) {
        return careerPaths.stream()
                .map(careerPath -> {
                    CareerPathProgressDetail detail = new CareerPathProgressDetail();
                    detail.setCareerPathName(careerPath.getName());
                    detail.setTotalSteps(careerPath.getTotalSteps());
                    detail.setCompletedSteps(careerPath.getCompletedSteps());
                    detail.setProgressPercentage((careerPath.getCompletedSteps() * 100.0) / careerPath.getTotalSteps());
                    return detail;
                })
                .collect(Collectors.toList());
    }
}