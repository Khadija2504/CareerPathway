package com.CareerPathway.CareerPathway.repository;

import com.CareerPathway.CareerPathway.model.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {
    List<Questionnaire> findBySkillId(Long skillId);
}
