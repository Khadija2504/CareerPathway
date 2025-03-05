package com.CareerPathway.CareerPathway.repository;

import com.CareerPathway.CareerPathway.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    @Query("SELECT DISTINCT t FROM Training t JOIN t.skillsCovered s WHERE s IN :skills")
    List<Training> findTrainingsBySkills(@Param("skills") List<String> skills);
}
