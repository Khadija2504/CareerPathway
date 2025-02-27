package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.dto.CourseDTO;
import com.CareerPathway.CareerPathway.mapper.CourseMapper;
import com.CareerPathway.CareerPathway.model.Course;
import com.CareerPathway.CareerPathway.model.enums.CourseType;
import com.CareerPathway.CareerPathway.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/courses")
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

    @PostMapping("/admin/createCourse")
    public ResponseEntity<?> createCourse(@RequestBody CourseDTO courseDTO) {
        Course course = courseMapper.toEntity(courseDTO);
        if(Objects.equals(courseDTO.getType(), "Doc")){
            course.setType(CourseType.DOC);
        } else {
            course.setType(CourseType.VIDEO);
        }
        Course SavedCourse = courseService.createCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(SavedCourse);
    }

    @PutMapping("/admin/updateCourse/{courseId}")
    public ResponseEntity<?> updateCourse(@RequestBody CourseDTO courseDTO, @PathVariable int courseId) {
        Course course = courseMapper.toEntity(courseDTO);
        Course updatedCourse = courseService.updateCourse(course, courseId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCourse);
    }

    @DeleteMapping("/admin/deleteCourse/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable int courseId) {
        boolean deletedCourse = courseService.deleteCourse(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(deletedCourse);
    }
}
