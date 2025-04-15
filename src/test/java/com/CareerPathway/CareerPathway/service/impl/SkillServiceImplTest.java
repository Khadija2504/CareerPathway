package com.CareerPathway.CareerPathway.service.impl;

import org.springframework.web.server.ResponseStatusException;
import com.CareerPathway.CareerPathway.model.Skill;
import com.CareerPathway.CareerPathway.repository.SkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SkillServiceImplTest {

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private SkillServiceImpl skillService;

    private Skill skill1;
    private Skill skill2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        skill1 = new Skill();
        skill1.setId(1L);
        skill1.setName("Java");
        skill1.setDescription("Java programming language");
        skill1.setCategory("Programming");

        skill2 = new Skill();
        skill2.setId(2L);
        skill2.setName("Spring Boot");
        skill2.setDescription("Spring Boot framework");
        skill2.setCategory("Framework");
    }

    @Test
    void testGetAllSkills_ShouldReturnSkills() {
        when(skillRepository.findAll()).thenReturn(Arrays.asList(skill1, skill2));

        List<Skill> skills = skillService.getAllSkills();

        assertNotNull(skills);
        assertEquals(2, skills.size());
    }

    @Test
    void testGetAllSkills_ShouldThrowExceptionWhenNoSkillsFound() {
        when(skillRepository.findAll()).thenReturn(Arrays.asList());

        assertThrows(ResponseStatusException.class, () -> skillService.getAllSkills());
    }

    @Test
    void testAddSkill_ShouldSaveAndReturnSkill() {
        when(skillRepository.save(any(Skill.class))).thenReturn(skill1);

        Skill savedSkill = skillService.addSkill(skill1);

        assertNotNull(savedSkill);
        assertEquals("Java", savedSkill.getName());
    }

    @Test
    void testUpdateSkill_ShouldUpdateAndReturnSkill() {
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill1));
        when(skillRepository.save(any(Skill.class))).thenReturn(skill1);

        Skill updatedSkill = new Skill();
        updatedSkill.setName("Advanced Java");
        updatedSkill.setDescription("Updated description");
        updatedSkill.setCategory("Programming");

        Skill result = skillService.updateSkill(updatedSkill, 1L);

        assertNotNull(result);
        assertEquals("Advanced Java", result.getName());
    }

    @Test
    void testUpdateSkill_ShouldThrowExceptionWhenNotFound() {
        when(skillRepository.findById(1L)).thenReturn(Optional.empty());

        Skill updatedSkill = new Skill();
        updatedSkill.setName("Advanced Java");

        assertThrows(ResponseStatusException.class, () -> skillService.updateSkill(updatedSkill, 1L));
    }

    @Test
    void testFindSkillById_ShouldReturnSkill() {
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill1));

        Skill result = skillService.findSkillById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testFindSkillById_ShouldThrowExceptionWhenNotFound() {
        when(skillRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> skillService.findSkillById(1L));
    }
}
