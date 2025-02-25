package com.CareerPathway.CareerPathway.repository;

import com.CareerPathway.CareerPathway.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAll(String courseName);
}
