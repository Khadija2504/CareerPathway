package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.Course;
import com.CareerPathway.CareerPathway.repository.CourseRepository;
import com.CareerPathway.CareerPathway.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }
}
