package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.Questionnaire;
import com.CareerPathway.CareerPathway.model.Skill;
import com.CareerPathway.CareerPathway.model.SkillAssessment;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.repository.QuestionnaireRepository;
import com.CareerPathway.CareerPathway.repository.SkillAssessmentRepository;
import com.CareerPathway.CareerPathway.service.QuestionnaireService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionnaireServiceImpl implements QuestionnaireService {
    private final QuestionnaireRepository questionnaireRepository;
    private final SkillAssessmentRepository skillAssessmentRepository;

    @Override
    public List<Questionnaire> getQuestionnairesBySkillId(Long skillId) {
        return questionnaireRepository.findBySkillId(skillId);
    }

    @Override
    public SkillAssessment submitQuestionnaireResponses(Long userId, Long skillId, List<String> responses) {
        List<Questionnaire> questionnaires = questionnaireRepository.findBySkillId(skillId);

        if (responses.size() != questionnaires.size()) {
            throw new IllegalArgumentException("Number of responses does not match the number of questions.");
        }

        int score = calculateScore(questionnaires, responses);

        SkillAssessment assessment = SkillAssessment.builder()
                .user(User.builder().id(userId).build())
                .skill(Skill.builder().id(skillId).build())
                .score(score)
                .assessmentDate(LocalDateTime.now())
                .build();

        return skillAssessmentRepository.save(assessment);
    }

    private int calculateScore(List<Questionnaire> questionnaires, List<String> responses) {
        int totalQuestions = questionnaires.size();
        int correctAnswers = 0;

        for (int i = 0; i < totalQuestions; i++) {
            String correctAnswer = questionnaires.get(i).getCorrectAnswer();
            String userResponse = responses.get(i);

            if (correctAnswer.equals(userResponse)) {
                correctAnswers++;
            }
        }

        return (int) ((correctAnswers / (double) totalQuestions) * 100);
    }

    private String classifySkillLevel(int score) {
        if (score >= 90) {
            return "Advanced";
        } else if (score >= 70) {
            return "Intermediate";
        } else {
            return "Beginner";
        }
    }
}