package com.CareerPathway.CareerPathway.repository;

import com.CareerPathway.CareerPathway.model.TrainingStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingStepRepository extends JpaRepository<TrainingStep, Long> {

}
