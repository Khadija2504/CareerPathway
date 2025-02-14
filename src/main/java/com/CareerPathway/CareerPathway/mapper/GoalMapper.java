package com.CareerPathway.CareerPathway.mapper;

import com.CareerPathway.CareerPathway.dto.EmployeeGoalDTO;
import com.CareerPathway.CareerPathway.model.EmployeeGoal;
import com.CareerPathway.CareerPathway.model.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GoalMapper {
    @Mapping(target = "employee", ignore = true)
    EmployeeGoal toEntity(EmployeeGoalDTO dto);

//    @AfterMapping
//    default void setUser(@MappingTarget EmployeeGoal goal, EmployeeGoalDTO dto) {
//        User user = new User();
//        user.setId(dto.getEmployeeId());
//        goal.setEmployee(user);
//    }
}
