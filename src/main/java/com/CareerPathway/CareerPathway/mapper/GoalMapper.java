package com.CareerPathway.CareerPathway.mapper;

import com.CareerPathway.CareerPathway.dto.EmployeeGoalDTO;
import com.CareerPathway.CareerPathway.model.EmployeeGoal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GoalMapper {
    @Mapping(target = "employee", ignore = true)
    EmployeeGoal toEntity(EmployeeGoalDTO dto);
}
