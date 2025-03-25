package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.dto.QuestionnaireDTO;
import com.CareerPathway.CareerPathway.mapper.QuestionnaireMapper;
import com.CareerPathway.CareerPathway.model.Questionnaire;
import com.CareerPathway.CareerPathway.model.Skill;
import com.CareerPathway.CareerPathway.model.SkillAssessment;
import com.CareerPathway.CareerPathway.service.QuestionnaireService;
import com.CareerPathway.CareerPathway.service.SkillService;
import com.CareerPathway.CareerPathway.service.TrainingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    private SkillService skillService;

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

            if(assessment.getScore() < 90) {
                trainingService.generateTrainingProgram(userId, skillId, assessment.getWeaknesses(), assessment.getSkillGaps(), assessment.getScore());
            }
            return ResponseEntity.ok(assessment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }

    @PostMapping("/admin/createQuestionnaire")
    public ResponseEntity<?> createQuestionnaire(@Valid @RequestBody QuestionnaireDTO questionnaireDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        System.out.println(questionnaireDTO);
        Questionnaire questionnaire = questionnaireMapper.toEntity(questionnaireDTO);
        System.out.println(questionnaire);
        Skill skill = skillService.findSkillById(questionnaireDTO.getSkillId());
        System.out.println(skill);
        questionnaire.setSkill(skill);
        System.out.println(questionnaire);
        Questionnaire savedQuestionnaire = questionnaireService.createQuestionnaire(questionnaire);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedQuestionnaire);
    }

    @GetMapping("/admin/getAllQuestionnaires")
    public ResponseEntity<List<Questionnaire>> getAllQuestionnaires() {
        List<Questionnaire> questionnaires = questionnaireService.getAllQuestionnaires();
        return ResponseEntity.status(HttpStatus.OK).body(questionnaires);
    }

    @GetMapping("/admin/deleteQuestionnaire/{id}")
    public ResponseEntity<?> deleteQuestionnaire(@PathVariable long id) {
        boolean deleted = questionnaireService.deleteQuestionnaire(id);
        return ResponseEntity.status(HttpStatus.OK).body(deleted);
    }
}