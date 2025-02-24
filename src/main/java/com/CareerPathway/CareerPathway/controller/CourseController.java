package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.model.Course;
import com.CareerPathway.CareerPathway.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:4200")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("getAllCourses")
    public List<Course> getAllCourses() {
        return courseService.fetchCoursesFromThirdPartyPlatforms();
    }
}