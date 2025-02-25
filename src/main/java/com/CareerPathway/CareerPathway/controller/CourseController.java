package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.dto.CourseDTO;
import com.CareerPathway.CareerPathway.mapper.CourseMapper;
import com.CareerPathway.CareerPathway.model.Course;
import com.CareerPathway.CareerPathway.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseMapper courseMapper;

    @GetMapping("/employee/displayAllCourses")
    public ResponseEntity<?> displayAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }

    @PostMapping("/admin/addCourse")
    public ResponseEntity<?> addCourse(@RequestBody CourseDTO courseDTO) {
        Course course = courseMapper.toEntity(courseDTO);
        Course SavedCourse = courseService.createCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(SavedCourse);
    }
}
