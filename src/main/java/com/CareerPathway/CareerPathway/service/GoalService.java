package com.CareerPathway.CareerPathway.service;

import com.CareerPathway.CareerPathway.model.EmployeeGoal;
import com.CareerPathway.CareerPathway.model.User;

import java.util.List;

public interface GoalService {
    EmployeeGoal addGoal(EmployeeGoal goal);
    List<EmployeeGoal> getGoals(Long id);
    EmployeeGoal updateGoalStatus(Long goalId, String status);
    boolean deleteGoal(Long goalId);
    boolean updateGoal(Long goalId, EmployeeGoal goal);
    EmployeeGoal getGoal(Long id);
    List<String> reminders(User user);
    EmployeeGoal updateEmployeeGoalSupported(boolean supported, long goalId);
    List<EmployeeGoal> getAllGoals();

}
