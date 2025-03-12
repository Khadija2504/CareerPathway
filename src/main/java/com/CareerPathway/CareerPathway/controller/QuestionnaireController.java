package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.dto.QuestionnaireDTO;
import com.CareerPathway.CareerPathway.mapper.QuestionnaireMapper;
import com.CareerPathway.CareerPathway.model.Questionnaire;
import com.CareerPathway.CareerPathway.model.SkillAssessment;
import com.CareerPathway.CareerPathway.service.QuestionnaireService;
import com.CareerPathway.CareerPathway.service.TrainingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employee/questionnaires")
@RequiredArgsConstructor
public class QuestionnaireController {

    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    private TrainingService trainingService;
    @Autowired
    private QuestionnaireMapper questionnaireMapper;

    @GetMapping("/skill/{skillId}")
    public ResponseEntity<List<Questionnaire>> getQuestionnairesBySkillId(@PathVariable Long skillId) {
        List<Questionnaire> questionnaires = questionnaireService.getQuestionnairesBySkillId(skillId);
        return ResponseEntity.ok(questionnaires);
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitQuestionnaireResponses(
            @RequestBody Map<String, Object> requestBody,
            HttpServletRequest request) {
        try {
            System.out.println("Request Body: " + requestBody);

            int userIdInt = Integer.parseInt(request.getAttribute("userId").toString());
            long userId = Long.parseLong(String.valueOf(userIdInt));

            Long skillId = Long.valueOf(requestBody.get("skillId").toString());
            List<String> responses = (List<String>) requestBody.get("responses");

            System.out.println("User ID: " + userId);
            System.out.println("Skill ID: " + skillId);
            System.out.println("Responses: " + responses);

            SkillAssessment assessment = questionnaireService.submitQuestionnaireResponses(userId, skillId, responses);

            trainingService.generateTrainingProgram(userId, skillId, assessment.getWeaknesses(), assessment.getSkillGaps(), assessment.getScore());

            return ResponseEntity.ok(assessment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }

    @PostMapping("/admin/createQuestionnaire")
    public ResponseEntity<?> createQuestionnaire(@RequestBody QuestionnaireDTO questionnaireDTO) {
        Questionnaire questionnaire = questionnaireMapper.toEntity(questionnaireDTO);
        Questionnaire savedQuestionnaire = questionnaireService.createQuestionnaire(questionnaire);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedQuestionnaire);
    }

    @GetMapping("/admin/getAllQuestionnaires")
    public ResponseEntity<List<Questionnaire>> getAllQuestionnaires() {
        List<Questionnaire> questionnaires = questionnaireService.getAllQuestionnaires();
        return ResponseEntity.status(HttpStatus.OK).body(questionnaires);
    }
}