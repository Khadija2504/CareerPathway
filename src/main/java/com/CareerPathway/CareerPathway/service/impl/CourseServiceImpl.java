package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.Course;
import com.CareerPathway.CareerPathway.repository.CourseRepository;
import com.CareerPathway.CareerPathway.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<Course> getAllCourses() {
        try {
            return courseRepository.findAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving courses", e);
        }
    }

    @Override
    public Course createCourse(Course course) {
        if (course == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course cannot be null");
        }

        try {
            return courseRepository.save(course);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating course", e);
        }
    }

    @Override
    public Course updateCourse(Course course, long courseId) {
        if (course == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course cannot be null");
        }

        try {
            Course oldCourse = courseRepository.findById(courseId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found with ID: " + courseId));

            oldCourse.setCategory(course.getCategory());
            oldCourse.setDescription(course.getDescription());
            oldCourse.setUrl(course.getUrl());
            oldCourse.setTitle(course.getTitle());
            oldCourse.setType(course.getType());

            return courseRepository.save(oldCourse);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating course", e);
        }
    }

    @Override
    public boolean deleteCourse(long courseId) {
        try {
            if (!courseRepository.existsById(courseId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found with ID: " + courseId);
            }

            courseRepository.deleteById(courseId);
            return true;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting course", e);
        }
    }

    @Override
    public Course getCourseById(long courseId) {
        try {
            return courseRepository.findById(courseId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found with ID: " + courseId));
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving course", e);
        }
    }
}
