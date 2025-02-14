package com.CareerPathway.CareerPathway.service;

import com.CareerPathway.CareerPathway.model.EmployeeGoal;

import java.util.List;

public interface GoalService {
    EmployeeGoal addGoal(EmployeeGoal goal);
    public List<EmployeeGoal> getGoals(Long id);

}
