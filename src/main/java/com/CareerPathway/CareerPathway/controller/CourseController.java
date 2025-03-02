package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.dto.CourseDTO;
import com.CareerPathway.CareerPathway.mapper.CourseMapper;
import com.CareerPathway.CareerPathway.model.Course;
import com.CareerPathway.CareerPathway.model.enums.CourseType;
import com.CareerPathway.CareerPathway.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseMapper courseMapper;

    private final String UPLOAD_DIR = "uploads/";

    @GetMapping("/employee/displayAllCourses")
    public ResponseEntity<?> displayAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }

    @PostMapping(value = "/admin/createCourse", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createCourse(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("type") String type,
            @RequestParam("category") String category,
            @RequestParam("file") MultipartFile file) {

        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Course course = new Course();
            course.setTitle(title);
            course.setDescription(description);
            course.setType(CourseType.valueOf(type));
            course.setCategory(category);
            course.setUrl("http://localhost:8800/uploads/" + fileName);

            Course savedCourse = courseService.createCourse(course);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }

    @GetMapping("/getCourseById/{courseId}")
    public ResponseEntity<?> getCourseById(@PathVariable int courseId) {
        Course course = courseService.getCourseById(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(course);
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