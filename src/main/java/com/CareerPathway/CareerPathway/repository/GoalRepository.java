package com.CareerPathway.CareerPathway.repository;

import com.CareerPathway.CareerPathway.model.EmployeeGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<EmployeeGoal, Integer>  {
    List<EmployeeGoal> findEmployeeGoalByEmployeeId(Long id);
}
