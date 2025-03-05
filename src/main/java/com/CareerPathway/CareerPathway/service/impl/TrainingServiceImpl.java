package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.*;
import com.CareerPathway.CareerPathway.model.enums.Level;
import com.CareerPathway.CareerPathway.repository.*;
import com.CareerPathway.CareerPathway.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrainingServiceImpl implements TrainingService {

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private SkillAssessmentRepository skillAssessmentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ResourceRepository resourceRepository;

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

        List<String> uniqueSkillsCovered = new ArrayList<>();
        uniqueSkillsCovered.add(skill.getName());

        Training training = Training.builder()
                .title(title)
                .description(description)
                .provider("Career Pathway Training Provider")
                .duration(duration)
                .level(level)
                .user(User.builder().id(userId).build())
                .skillsCovered(uniqueSkillsCovered)
                .createdDate(LocalDateTime.now())
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

    @Override
    public Map<String, Object> getRecommendations(Long userId) {
        List<SkillAssessment> assessments = skillAssessmentRepository.findByUserId(userId);

        Set<String> skills = assessments.stream()
                .map(a -> a.getSkill().getName())
                .collect(Collectors.toSet());

        List<Training> trainingPrograms = trainingRepository.findTop1ByUserIdOrderByCreatedDateDesc(userId);
        Set<String> skillsCoveredInTraining = trainingPrograms.stream()
                .flatMap(t -> t.getSkillsCovered().stream())
                .collect(Collectors.toSet());

        Set<String> targetSkills = new HashSet<>(skills);
        targetSkills.addAll(skillsCoveredInTraining);

        if (targetSkills.isEmpty()) {
            targetSkills.add("General");
        }

        List<Course> courses = courseRepository.findByCategoryIn(new ArrayList<>(targetSkills));
        List<Resource> resources = resourceRepository.findByCategoryIn(new ArrayList<>(targetSkills));

        Map<String, Object> response = new HashMap<>();
        response.put("courses", courses);
        response.put("resources", resources);
        response.put("trainingPrograms", trainingPrograms);
        return response;
    }

    @Override
    public List<Training> getAdditionalTrainingPrograms(Long userId, int page, int size) {
        System.out.println("Fetching additional training programs for User ID: " + userId);
        System.out.println("Page: " + page + ", Size: " + size);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

        Page<Training> trainingPage = trainingRepository.findByUser_Id(userId, pageable);

        System.out.println("Fetched " + trainingPage.getNumberOfElements() + " training programs");

        return trainingPage.getContent();
    }
}