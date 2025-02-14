package com.CareerPathway.CareerPathway.repository;

import com.CareerPathway.CareerPathway.model.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {
    List<Questionnaire> findBySkillId(Long skillId);
}
