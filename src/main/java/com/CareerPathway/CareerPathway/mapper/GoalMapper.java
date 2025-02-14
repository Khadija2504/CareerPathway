package com.CareerPathway.CareerPathway.mapper;

import com.CareerPathway.CareerPathway.dto.EmployeeGoalDTO;
import com.CareerPathway.CareerPathway.model.EmployeeGoal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GoalMapper {
    EmployeeGoal toEntity(EmployeeGoalDTO employeeGoalDTO);
    EmployeeGoalDTO toDto(EmployeeGoal employeeGoal);
}
