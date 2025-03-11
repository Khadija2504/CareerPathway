package com.CareerPathway.CareerPathway.service;

import com.CareerPathway.CareerPathway.model.Employee;
import com.CareerPathway.CareerPathway.model.Skill;
import com.CareerPathway.CareerPathway.model.SkillAssessment;

import java.util.List;

public interface SkillService {
    List<Skill> getAllSkills();
    List<SkillAssessment> getAllEmployeeSkillAssessments(Long userId);
    Skill addSkill(Skill skill);

}
