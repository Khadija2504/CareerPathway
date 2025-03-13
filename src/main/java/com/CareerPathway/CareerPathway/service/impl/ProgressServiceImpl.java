package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.dto.ProgressMetricsDTO;
import com.CareerPathway.CareerPathway.model.*;
import com.CareerPathway.CareerPathway.repository.*;
import com.CareerPathway.CareerPathway.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

        List<CareerPath> careerPaths = careerPathRepository.findCareerPathByEmployee(employee);
        metrics.setCareerPathProgress(calculateCareerPathProgress(careerPaths));

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
        long completedGoals = goals.stream().filter(g -> "Completed".equals(g.getStatus())).count();
        return (goals.size() == 0) ? 0 : (completedGoals * 100.0) / goals.size();
    }
}
