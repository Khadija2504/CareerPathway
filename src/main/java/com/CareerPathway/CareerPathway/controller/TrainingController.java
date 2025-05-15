package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.dto.TrainingStepDTO;
import com.CareerPathway.CareerPathway.mapper.TrainingStepMapper;
import com.CareerPathway.CareerPathway.model.Training;
import com.CareerPathway.CareerPathway.model.TrainingStep;
import com.CareerPathway.CareerPathway.service.TrainingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/training")
public class TrainingController {

    @Autowired
    private TrainingService trainingService;
    @Autowired
    private TrainingStepMapper trainingStepMapper;

    @GetMapping("/recommendations")
    public ResponseEntity<Map<String, Object>> getRecommendations(HttpServletRequest request) {
        long userId = Long.parseLong(request.getAttribute("userId").toString());
        return ResponseEntity.ok(trainingService.getRecommendations(userId));
    }

    @GetMapping("/training-programs")
    public ResponseEntity<List<Training>> getAdditionalTrainingPrograms(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size) {
        long userId = Long.parseLong(request.getAttribute("userId").toString());
        List<Training> trainingPrograms = trainingService.getAdditionalTrainingPrograms(userId, page, size);
        return ResponseEntity.ok(trainingPrograms);
    }

    @PostMapping("/training-program-steps")
    public ResponseEntity<?> addTrainingProgramSteps(HttpServletRequest request, @RequestBody List<TrainingStepDTO> trainingStepsDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        long mentorId = Long.parseLong(request.getAttribute("userId").toString());
        List<TrainingStep> trainingSteps = new ArrayList<>();
        for (TrainingStepDTO trainingStepDTO : trainingStepsDTO) {
            trainingSteps.add(trainingStepMapper.toEntity(trainingStepDTO));
        }
        List<TrainingStep> savedTrainingSteps = trainingService.createTrainingStep(trainingSteps.stream().findFirst().get().getTraining().getId(), mentorId, trainingSteps);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTrainingSteps);
    }
}
