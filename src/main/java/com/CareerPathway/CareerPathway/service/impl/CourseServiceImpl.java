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

    @Override
    public Course updateCourse(Course course, long courseId){
        Course oldCourse = courseRepository.findById(courseId).get();
        oldCourse.setCategory(course.getCategory());
        oldCourse.setDescription(course.getDescription());
        oldCourse.setUrl(course.getUrl());
        oldCourse.setTitle(course.getTitle());
        oldCourse.setType(course.getType());
        return courseRepository.save(oldCourse);
    };

    @Override
    public boolean deleteCourse(long courseId) {
        try{
            courseRepository.deleteById(courseId);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public Course getCourseById(long courseId) {
        return courseRepository.findById(courseId).get();
    }
}
