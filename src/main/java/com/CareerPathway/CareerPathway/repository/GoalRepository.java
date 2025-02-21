package com.CareerPathway.CareerPathway.repository;

import com.CareerPathway.CareerPathway.model.EmployeeGoal;
import com.CareerPathway.CareerPathway.model.enums.GoalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface GoalRepository extends JpaRepository<EmployeeGoal, Integer>  {
    List<EmployeeGoal> findEmployeeGoalByEmployeeId(Long id);
    Optional<EmployeeGoal> findEmployeeGoalById(Long id);
    List<EmployeeGoal> findEmployeeGoalByEmployeeIdAndStatusIn(Long employee_id, Collection<GoalStatus> status);
}
