package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.dto.EmployeeGoalDTO;
import com.CareerPathway.CareerPathway.model.EmployeeGoal;
import com.CareerPathway.CareerPathway.repository.GoalRepository;
import com.CareerPathway.CareerPathway.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoalServiceImpl implements GoalService {
    @Autowired
    private GoalRepository goalRepository;

    @Override
    public EmployeeGoal addGoal(EmployeeGoal goal) {
        if(goal.getEmployee() == null || goal.getEmployee().getId() == null) {
            throw new IllegalArgumentException("Goal must be associated with a valid user");
        }
        return goalRepository.save(goal);
    }

    @Override
    public List<EmployeeGoal> getGoals(Long id) {
        return goalRepository.findEmployeeGoalByEmployeeId(id);
    }
}
