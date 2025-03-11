package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.Skill;
import com.CareerPathway.CareerPathway.model.SkillAssessment;
import com.CareerPathway.CareerPathway.repository.SkillAssessmentRepository;
import com.CareerPathway.CareerPathway.repository.SkillRepository;
import com.CareerPathway.CareerPathway.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillServiceImpl implements SkillService {
    @Autowired
    SkillRepository skillRepository;
    @Autowired
    SkillAssessmentRepository skillAssessmentRepository;
    @Override
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    @Override
    public List<SkillAssessment> getAllEmployeeSkillAssessments(Long userId) {
        return skillAssessmentRepository.findByUserId(userId);
    }

    @Override
    public Skill addSkill(Skill skill) {
        return skillRepository.save(skill);
    }

}
