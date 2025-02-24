package com.CareerPathway.CareerPathway.service;

import com.CareerPathway.CareerPathway.model.Course;

import java.util.List;

public interface CourseService {
    List<Course> fetchCoursesFromThirdPartyPlatforms();
    List<Course> fetchUdemyCourses();
    List<Course> fetchLinkedInCourses();
    List<Course> fetchCourseraCourses();
}
