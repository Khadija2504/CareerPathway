package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.Skill;
import com.CareerPathway.CareerPathway.model.SkillAssessment;
import com.CareerPathway.CareerPathway.repository.SkillAssessmentRepository;
import com.CareerPathway.CareerPathway.repository.SkillRepository;
import com.CareerPathway.CareerPathway.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SkillServiceImpl implements SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SkillAssessmentRepository skillAssessmentRepository;

    @Override
    public List<Skill> getAllSkills() {
        List<Skill> skills = skillRepository.findAll();
        if (skills.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No skills found.");
        }
        return skills;
    }

    @Override
    public List<SkillAssessment> getAllEmployeeSkillAssessments(Long userId) {
        return skillAssessmentRepository.findByUserId(userId);
    }

    @Override
    public Skill addSkill(Skill skill) {
        try {
            return skillRepository.save(skill);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Error saving skill: Data integrity violation.", e);
        } catch (Exception e) {
            throw new RuntimeException("Error saving skill.", e);
        }
    }

    @Override
    public Skill updateSkill(Skill skill, Long id) {
        Skill existingSkill = skillRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Skill with ID " + id + " not found."));

        existingSkill.setName(skill.getName());
        existingSkill.setDescription(skill.getDescription());
        existingSkill.setCategory(skill.getCategory());

        try {
            return skillRepository.save(existingSkill);
        } catch (Exception e) {
            throw new RuntimeException("Error updating skill.", e);
        }
    }

    @Override
    public boolean deleteSkill(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Skill with ID " + id + " not found."));

        try {
            skillRepository.delete(skill);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting skill.", e);
        }
    }

    @Override
    public Skill findSkillById(Long id) {
        return skillRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Skill with ID " + id + " not found."));
    }
}
