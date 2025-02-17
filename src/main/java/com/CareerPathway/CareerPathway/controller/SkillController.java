package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.model.EmployeeGoal;
import com.CareerPathway.CareerPathway.model.Skill;
import com.CareerPathway.CareerPathway.service.SkillService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee/skills")
@RequiredArgsConstructor
public class SkillController {
    @Autowired
    private SkillService skillService;

    @GetMapping("displaySkills")
    public ResponseEntity<?> getAllSkills(HttpServletRequest request) {
        List<Skill> goals = skillService.getAllSkills();
        return ResponseEntity.status(HttpStatus.OK).body(goals);
    }
}
