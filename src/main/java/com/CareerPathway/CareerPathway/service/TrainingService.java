package com.CareerPathway.CareerPathway.service;

import com.CareerPathway.CareerPathway.model.Training;

import java.util.List;
import java.util.Map;

public interface TrainingService {
    void generateTrainingProgram(Long userId, Long skillId, List<String> weaknesses, List<String> skillGaps, int score);
    int calculateDuration(List<String> weaknesses, List<String> skillGaps);
    int getTopicDuration(String topic);
    String generateTrainingTitle(String skillName, List<String> weaknesses);
    String generateTrainingDescription(String skillName, List<String> weaknesses, List<String> skillGaps);
    Map<String, Object> getRecommendations(Long userId);
    List<Training> getAdditionalTrainingPrograms(Long userId, int page, int size);
}
