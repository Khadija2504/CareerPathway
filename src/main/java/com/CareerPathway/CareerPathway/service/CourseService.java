package com.CareerPathway.CareerPathway.service;

import com.CareerPathway.CareerPathway.model.Course;

import java.util.List;

public interface CourseService {
    List<Course> getAllCourses();
    Course createCourse(Course course);
    Course updateCourse(Course course, long courseId);
    boolean deleteCourse(long courseId);
    Course getCourseById(long courseId);
}
