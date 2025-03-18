package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.*;
import com.CareerPathway.CareerPathway.repository.QuestionnaireRepository;
import com.CareerPathway.CareerPathway.repository.SkillAssessmentRepository;
import com.CareerPathway.CareerPathway.service.QuestionnaireService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        if (questionnaires.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No questionnaires found for skill ID: " + skillId);
        }
        if (responses.size() != questionnaires.size()) {
            throw new IllegalArgumentException("Number of responses does not match the number of questions.");
        }

        int score = calculateScore(questionnaires, responses);
        List<String> strengths = new ArrayList<>();
        List<String> weaknesses = new ArrayList<>();
        List<String> skillGaps = new ArrayList<>();

        for (int i = 0; i < questionnaires.size(); i++) {
            String correctAnswer = questionnaires.get(i).getCorrectAnswer();
            String userResponse = responses.get(i);

            if (correctAnswer.equals(userResponse)) {
                strengths.add(questionnaires.get(i).getQuestionText());
            } else {
                weaknesses.add(questionnaires.get(i).getQuestionText());
                skillGaps.add(questionnaires.get(i).getQuestionText());
            }
        }

        SkillAssessment assessment = SkillAssessment.builder()
                .user(User.builder().id(userId).build())
                .skill(Skill.builder().id(skillId).build())
                .score(score)
                .assessmentDate(LocalDateTime.now())
                .strengths(strengths)
                .weaknesses(weaknesses)
                .skillGaps(skillGaps)
                .build();

        try {
            return skillAssessmentRepository.save(assessment);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save skill assessment.", e);
        }
    }

    private int calculateScore(List<Questionnaire> questionnaires, List<String> responses) {
        int totalQuestions = questionnaires.size();
        int correctAnswers = 0;

        for (int i = 0; i < totalQuestions; i++) {
            if (questionnaires.get(i).getCorrectAnswer().equals(responses.get(i))) {
                correctAnswers++;
            }
        }

        return (int) ((correctAnswers / (double) totalQuestions) * 100);
    }

    @Override
    public Questionnaire createQuestionnaire(Questionnaire questionnaire) {
        return questionnaireRepository.save(questionnaire);
    }

    @Override
    public List<Questionnaire> getAllQuestionnaires() {
        return questionnaireRepository.findAll();
    }

    @Override
    public boolean deleteQuestionnaire(long questionnaireId) {
        try {
            questionnaireRepository.deleteById(questionnaireId);
            return true;
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Questionnaire not found with ID: " + questionnaireId);
        }
    }
}