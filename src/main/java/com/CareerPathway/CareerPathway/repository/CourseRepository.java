package com.CareerPathway.CareerPathway.repository;

import com.CareerPathway.CareerPathway.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c WHERE c.category IN :categories")
    List<Course> findByCategoryIn(@Param("categories") List<String> categories);}
