package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.model.Training;
import com.CareerPathway.CareerPathway.service.TrainingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/training")
public class TrainingController {

    @Autowired
    private TrainingService trainingService;

//    @GetMapping("/recommendations")
//    public ResponseEntity<Map<String, Object>> getRecommendations(HttpServletRequest request) {
//        long userId = Long.parseLong(request.getAttribute("userId").toString());
//        return ResponseEntity.ok(trainingService.getRecommendations(userId));
//    }
}
