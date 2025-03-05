package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.*;
import com.CareerPathway.CareerPathway.model.enums.Level;
import com.CareerPathway.CareerPathway.repository.*;
import com.CareerPathway.CareerPathway.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrainingServiceImpl implements TrainingService {

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Override
    public void generateTrainingProgram(Long userId, Long skillId, List<String> weaknesses, List<String> skillGaps, int score) {
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill not found with ID: " + skillId));

        Level level;
        if (score < 40) {
            level = Level.BEGINNER;
        } else if (score >= 40 && score <= 70) {
            level = Level.INTERMEDIATE;
        } else {
            level = Level.ADVANCED;
        }

        int duration = calculateDuration(weaknesses, skillGaps);

        String title = generateTrainingTitle(skill.getName(), weaknesses);
        String description = generateTrainingDescription(skill.getName(), weaknesses, skillGaps);

        List<String> uniqueSkillsCovered = new ArrayList<>(skillGaps);

        Training training = Training.builder()
                .title(title)
                .description(description)
                .provider("Career Pathway Training Provider")
                .duration(duration)
                .level(level)
                .user(User.builder().id(userId).build())
                .skillsCovered(uniqueSkillsCovered)
                .build();

        trainingRepository.save(training);
    }

    @Override
    public int calculateDuration(List<String> weaknesses, List<String> skillGaps) {
        if (weaknesses == null) weaknesses = Collections.emptyList();
        if (skillGaps == null) skillGaps = Collections.emptyList();

        int totalDuration = 0;
        for (String topic : weaknesses) {
            totalDuration += getTopicDuration(topic);
        }
        for (String topic : skillGaps) {
            totalDuration += getTopicDuration(topic);
        }
        return totalDuration;
    }

    @Override
    public int getTopicDuration(String topic) {
        if (topic == null || topic.trim().isEmpty()) {
            return 0;
        }

        switch (topic.toLowerCase()) {
            case "exception handling":
                return 6;
            case "multithreading":
                return 8;
            case "stream api":
                return 5;
            case "lambda expressions":
                return 4;
            default:
                return 5;
        }
    }

    @Override
    public String generateTrainingTitle(String skillName, List<String> weaknesses) {
        if (weaknesses == null || weaknesses.isEmpty()) {
            return skillName + ": General Training Program";
        }
        return String.format("%s: Mastering %s", skillName, String.join(", ", weaknesses));
    }

    @Override
    public String generateTrainingDescription(String skillName, List<String> weaknesses, List<String> skillGaps) {
        if (weaknesses == null) weaknesses = Collections.emptyList();
        if (skillGaps == null) skillGaps = Collections.emptyList();

        return String.format("This training program is designed to improve your %s skills by focusing on the following areas: %s. It will also address the following skill gaps: %s.",
                skillName, String.join(", ", weaknesses), String.join(", ", skillGaps));
    }
}