package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.dto.TrainingStepDTO;
import com.CareerPathway.CareerPathway.mapper.TrainingStepMapper;
import com.CareerPathway.CareerPathway.model.Training;
import com.CareerPathway.CareerPathway.model.TrainingStep;
import com.CareerPathway.CareerPathway.service.TrainingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TrainingController {

    @Autowired
    private TrainingService trainingService;
    @Autowired
    private TrainingStepMapper trainingStepMapper;

    @GetMapping("/training/recommendations")
    public ResponseEntity<Map<String, Object>> getRecommendations(HttpServletRequest request) {
        long userId = Long.parseLong(request.getAttribute("userId").toString());
        return ResponseEntity.ok(trainingService.getRecommendations(userId));
    }

    @GetMapping("/training/training-programs")
    public ResponseEntity<List<Training>> getAdditionalTrainingPrograms(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size) {
        long userId = Long.parseLong(request.getAttribute("userId").toString());
        List<Training> trainingPrograms = trainingService.getAdditionalTrainingPrograms(userId, page, size);
        return ResponseEntity.ok(trainingPrograms);
    }

    @PostMapping("/mentee/training-program-steps")
    public ResponseEntity<?> addTrainingProgramSteps(HttpServletRequest request, @RequestBody List<TrainingStepDTO> trainingStepsDTO, BindingResult result) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        List<String> validationErrors = new ArrayList<>();

        for (int i = 0; i < trainingStepsDTO.size(); i++) {
            TrainingStepDTO dto = trainingStepsDTO.get(i);
            Set<ConstraintViolation<TrainingStepDTO>> violations = validator.validate(dto);
            for (ConstraintViolation<TrainingStepDTO> violation : violations) {
                validationErrors.add("Step " + (i + 1) + " - " + violation.getPropertyPath() + ": " + violation.getMessage());
            }
        }

        if (!validationErrors.isEmpty()) {
            return ResponseEntity.badRequest().body(validationErrors);
        }
        long mentorId = Long.parseLong(request.getAttribute("userId").toString());
        List<TrainingStep> trainingSteps = new ArrayList<>();
        for (TrainingStepDTO trainingStepDTO : trainingStepsDTO) {
            trainingSteps.add(trainingStepMapper.toEntity(trainingStepDTO));
        }
        List<TrainingStep> savedTrainingSteps = trainingService.createTrainingStep(trainingSteps.stream().findFirst().get().getTraining().getId(), mentorId, trainingSteps);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTrainingSteps);
    }

    @GetMapping("/mentee/mentee-training-program/{menteeId}")
    public ResponseEntity<List<Training>> menteeTrainingPrograms(@PathVariable long menteeId) {
        System.out.println("inside the controller");
        List<Training> trainingPrograms = trainingService.getEmployeeTrainings(menteeId);
        System.out.println(trainingPrograms);
        return ResponseEntity.status(HttpStatus.FOUND).body(trainingPrograms);
    }
}
