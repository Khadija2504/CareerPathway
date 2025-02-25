package com.CareerPathway.CareerPathway.mapper;

import com.CareerPathway.CareerPathway.dto.CourseDTO;
import com.CareerPathway.CareerPathway.model.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    Course toEntity(CourseDTO courseDTO);
    CourseDTO toDTO(Course course);
}
