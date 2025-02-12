package com.CareerPathway.CareerPathway.repository;

import com.CareerPathway.CareerPathway.model.SkillAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SkillAssessmentRepository extends JpaRepository<SkillAssessment, Long> {
    List<SkillAssessment> findByUserId(Long userId);
}
