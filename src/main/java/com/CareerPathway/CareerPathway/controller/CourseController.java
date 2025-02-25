package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.model.Course;
import com.CareerPathway.CareerPathway.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/employee/displayAllCourses")
    public ResponseEntity<?> displayAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }
}
