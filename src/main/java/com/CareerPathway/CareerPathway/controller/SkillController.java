package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.dto.SkillDTO;
import com.CareerPathway.CareerPathway.mapper.SkillMapper;
import com.CareerPathway.CareerPathway.model.EmployeeGoal;
import com.CareerPathway.CareerPathway.model.Skill;
import com.CareerPathway.CareerPathway.model.SkillAssessment;
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
    @Autowired
    private SkillMapper skillMapper;

    @GetMapping("displaySkills")
    public ResponseEntity<?> getAllSkills() {
        List<Skill> skills = skillService.getAllSkills();
        return ResponseEntity.status(HttpStatus.OK).body(skills);
    }

    @GetMapping("displayEmployeeSkillAssessment")
    public ResponseEntity<?> getAllEmployeeSkillAssessments(HttpServletRequest request) {
        int userIdInt = Integer.parseInt(request.getAttribute("userId").toString());
        long userId = Long.parseLong(String.valueOf(userIdInt));
        List<SkillAssessment> skillAssessments = skillService.getAllEmployeeSkillAssessments(userId);
        return ResponseEntity.status(HttpStatus.OK).body(skillAssessments);
    }

    @PostMapping("/admin/addSkill")
    public ResponseEntity<?> addSkill(@RequestBody SkillDTO skillDTO) {
        Skill skill = skillMapper.toEntity(skillDTO);
        Skill savedSkill = skillService.addSkill(skill);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSkill);
    }

}
