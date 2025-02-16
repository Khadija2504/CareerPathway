package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.dto.EmployeeGoalDTO;
import com.CareerPathway.CareerPathway.dto.GoalUpdateDTO;
import com.CareerPathway.CareerPathway.mapper.GoalMapper;
import com.CareerPathway.CareerPathway.model.EmployeeGoal;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.service.GoalService;
import com.CareerPathway.CareerPathway.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employee/goal")
public class GoalController {
    @Autowired
    private GoalService goalService;

    @Autowired
    private GoalMapper goalMapper;
    @Autowired
    private UserService userService;

    @PostMapping("/addGoal")
    public ResponseEntity<?> addGoal(HttpServletRequest request, @Valid @RequestBody EmployeeGoalDTO employeeGoalDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        int userId = Integer.parseInt(request.getAttribute("userId").toString());

        User user = userService.userDetails(userId);

        EmployeeGoal employeeGoal = goalMapper.toEntity(employeeGoalDTO);
        employeeGoal.setEmployee(user);

        EmployeeGoal savedGoal = goalService.addGoal(employeeGoal);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGoal);
    }

    @GetMapping("displayEmployeeGoals")
    public ResponseEntity<?> getAllGoals(HttpServletRequest request) {
        int userId = Integer.parseInt(request.getAttribute("userId").toString());
        List<EmployeeGoal> goals = goalService.getGoals((long) userId);
        return ResponseEntity.status(HttpStatus.OK).body(goals);
    }

    @PostMapping("updateGoalStatus")
    public ResponseEntity<?> updateGoalStatus(HttpServletRequest request, @Valid @RequestBody GoalUpdateDTO goalData) {
        goalService.updateGoalStatus(goalData.getGoalId(), goalData.getStatus());
        return ResponseEntity.status(HttpStatus.OK).body(goalData);
    }

    @PostMapping("deleteGoal")
    public ResponseEntity<?> deleteGoal(HttpServletRequest request, @RequestBody Integer goalId) {
        boolean isDeleted = goalService.deleteGoal(goalId.longValue());
        if(isDeleted){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new String[]{"Deleted Goal"});
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new String[]{"Error while deleting Goal"});
        }
    }

    @PutMapping("updateGoal/{goalId}")
    public ResponseEntity<?> updateGoal(HttpServletRequest request, @Valid @RequestBody EmployeeGoalDTO employeeGoalDTO, BindingResult result, @PathVariable Long goalId) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        int userId = Integer.parseInt(request.getAttribute("userId").toString());
        User user = userService.userDetails(userId);
        EmployeeGoal employeeGoal = goalMapper.toEntity(employeeGoalDTO);
        employeeGoal.setEmployee(user);
        boolean updatedGoal = goalService.updateGoal(goalId, employeeGoal);
        if(updatedGoal){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new String[]{"Updated Goal"});
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new String[]{"Error while updating Goal"});
        }
    }

    @GetMapping("getGoal/{goalId}")
    public ResponseEntity<?> getGoal(@PathVariable Long goalId) {
        EmployeeGoal goals = goalService.getGoal(goalId);
        if (goals == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new String[]{"Goal not found"});
        }
        return ResponseEntity.status(HttpStatus.OK).body(goals);
    }
}
