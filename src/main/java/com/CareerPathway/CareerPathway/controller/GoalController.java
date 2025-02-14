package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.dto.EmployeeGoalDTO;
import com.CareerPathway.CareerPathway.mapper.GoalMapper;
import com.CareerPathway.CareerPathway.model.EmployeeGoal;
import com.CareerPathway.CareerPathway.service.GoalService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employee/goal")
public class GoalController {
    @Autowired
    private GoalService goalService;

    @Autowired
    private GoalMapper goalMapper;

    @PostMapping("/addGoal")
    public ResponseEntity<?> addGoal(HttpServletRequest request, @Valid @RequestBody EmployeeGoalDTO employeeGoalDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        EmployeeGoal employeeGoal = goalMapper.toEntity(employeeGoalDTO);
        EmployeeGoal savedGoal = goalService.addGoal(employeeGoal);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGoal);
    }
}
