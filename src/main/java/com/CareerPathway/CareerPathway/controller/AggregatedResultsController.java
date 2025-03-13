package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.dto.AggregatedResultDTO;
import com.CareerPathway.CareerPathway.dto.ProgressMetricsDTO;
import com.CareerPathway.CareerPathway.service.ProgressService;
import com.CareerPathway.CareerPathway.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AggregatedResultsController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProgressService progressService;

    @GetMapping("/aggregated-results")
    public ResponseEntity<List<AggregatedResultDTO>> getAggregatedResults() {
        List<AggregatedResultDTO> results = userService.calculateAggregatedResults();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/progress-metrics")
    public ResponseEntity<ProgressMetricsDTO> getProgressMetrics(HttpServletRequest request) {
        long employeeId = Integer.parseInt(request.getAttribute("userId").toString());

        ProgressMetricsDTO metrics = progressService.calculateProgressMetrics(employeeId);

        return ResponseEntity.status(HttpStatus.OK).body(metrics);
    }

    @GetMapping("/reports/{employeeId}")
    public ResponseEntity<ProgressMetricsDTO> getReports(@PathVariable long employeeId) {

        ProgressMetricsDTO metrics = progressService.calculateProgressMetrics(employeeId);

        return ResponseEntity.status(HttpStatus.OK).body(metrics);
    }
}
