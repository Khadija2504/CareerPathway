package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.model.Questionnaire;
import com.CareerPathway.CareerPathway.model.SkillAssessment;
import com.CareerPathway.CareerPathway.service.QuestionnaireService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee/questionnaires")
@RequiredArgsConstructor
public class QuestionnaireController {
    private final QuestionnaireService questionnaireService;

    @GetMapping("/skill/{skillId}")
    public ResponseEntity<List<Questionnaire>> getQuestionnairesBySkillId(@PathVariable Long skillId) {
        List<Questionnaire> questionnaires = questionnaireService.getQuestionnairesBySkillId(skillId);
        return ResponseEntity.ok(questionnaires);
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitQuestionnaireResponses(
            @RequestParam Long userId,
            @RequestParam Long skillId,
            @RequestBody List<String> responses) {
        try {
            SkillAssessment assessment = questionnaireService.submitQuestionnaireResponses(userId, skillId, responses);
            return ResponseEntity.ok(assessment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }
}
