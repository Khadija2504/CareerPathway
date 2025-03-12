package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.dto.AggregatedResultDTO;
import com.CareerPathway.CareerPathway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AggregatedResultsController {

    @Autowired
    private UserService userService;

    @GetMapping("/aggregated-results")
    public ResponseEntity<List<AggregatedResultDTO>> getAggregatedResults() {
        List<AggregatedResultDTO> results = userService.calculateAggregatedResults();
        return ResponseEntity.ok(results);
    }
}
