package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.Skill;
import com.CareerPathway.CareerPathway.repository.SkillRepository;
import com.CareerPathway.CareerPathway.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillServiceImpl implements SkillService {
    @Autowired
    SkillRepository skillRepository;
    @Override
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }
}
