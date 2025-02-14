package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.EmployeeGoal;
import com.CareerPathway.CareerPathway.model.enums.GoalStatus;
import com.CareerPathway.CareerPathway.repository.GoalRepository;
import com.CareerPathway.CareerPathway.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public EmployeeGoal updateGoalStatus(Long goalId, String status) {
        Optional<EmployeeGoal> goalOpt = goalRepository.findEmployeeGoalById(goalId);
        if(goalOpt.isPresent()) {
            EmployeeGoal  goal= goalOpt.get();
            if(status.equals("COMPLETED")) {
                goal.setStatus(GoalStatus.COMPLETED);
            } else {
                goal.setStatus(GoalStatus.IN_PROGRESS);
            }
            return goalRepository.save(goal);
        } else {
            throw new IllegalArgumentException("Goal is not associated with a valid user");
        }
    }
}
