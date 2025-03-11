package com.CareerPathway.CareerPathway.service;

import com.CareerPathway.CareerPathway.model.Questionnaire;
import com.CareerPathway.CareerPathway.model.SkillAssessment;

import java.util.List;

public interface QuestionnaireService {
    List<Questionnaire> getQuestionnairesBySkillId(Long skillId);
    SkillAssessment submitQuestionnaireResponses(Long userId, Long skillId, List<String> responses);
    Questionnaire createQuestionnaire(Questionnaire questionnaire);
}
