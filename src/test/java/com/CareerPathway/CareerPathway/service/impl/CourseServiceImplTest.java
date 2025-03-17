package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.Course;
import com.CareerPathway.CareerPathway.model.enums.CourseType;
import com.CareerPathway.CareerPathway.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Course createCourse() {
        Course course = new Course();
        course.setId(1L);
        course.setTitle("Java Programming");
        course.setDescription("Learn Java from scratch");
        course.setUrl("https://example.com/java-course");
        course.setType(CourseType.VIDEO);
        course.setCategory("Programming");
        return course;
    }

    @Test
    void getAllCourses_Success() {
        Course course1 = createCourse();
        Course course2 = createCourse();
        course2.setId(2L);
        course2.setTitle("Python Programming");

        when(courseRepository.findAll()).thenReturn(Arrays.asList(course1, course2));

        List<Course> result = courseService.getAllCourses();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void getAllCourses_Error() {
        when(courseRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            courseService.getAllCourses();
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error retrieving courses", exception.getReason());
    }

    @Test
    void createCourse_Success() {
        Course course = createCourse();

        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course result = courseService.createCourse(course);

        assertNotNull(result);
        assertEquals("Java Programming", result.getTitle());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void createCourse_NullCourse() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            courseService.createCourse(null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Course cannot be null", exception.getReason());
    }

    @Test
    void createCourse_Error() {
        Course course = createCourse();

        when(courseRepository.save(any(Course.class))).thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            courseService.createCourse(course);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error creating course", exception.getReason());
    }

    @Test
    void updateCourse_Success() {
        Course existingCourse = createCourse();
        Course updatedCourse = createCourse();
        updatedCourse.setTitle("Advanced Java Programming");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(updatedCourse);

        Course result = courseService.updateCourse(updatedCourse, 1L);

        assertNotNull(result);
        assertEquals("Advanced Java Programming", result.getTitle());
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(existingCourse);
    }

    @Test
    void updateCourse_NullCourse() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            courseService.updateCourse(null, 1L);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Course cannot be null", exception.getReason());
    }

    @Test
    void updateCourse_NotFound() {
        Course updatedCourse = createCourse();

        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            courseService.updateCourse(updatedCourse, 1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Course not found with ID: 1", exception.getReason());
    }

    @Test
    void updateCourse_Error() {
        Course existingCourse = createCourse();
        Course updatedCourse = createCourse();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.save(any(Course.class))).thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            courseService.updateCourse(updatedCourse, 1L);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error updating course", exception.getReason());
    }

    @Test
    void deleteCourse_Success() {
        when(courseRepository.existsById(1L)).thenReturn(true);

        boolean result = courseService.deleteCourse(1L);

        assertTrue(result);
        verify(courseRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCourse_NotFound() {
        when(courseRepository.existsById(1L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            courseService.deleteCourse(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Course not found with ID: 1", exception.getReason());
    }

    @Test
    void deleteCourse_Error() {
        when(courseRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(courseRepository).deleteById(1L);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            courseService.deleteCourse(1L);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error deleting course", exception.getReason());
    }

    @Test
    void getCourseById_Success() {
        Course course = createCourse();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course result = courseService.getCourseById(1L);

        assertNotNull(result);
        assertEquals("Java Programming", result.getTitle());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void getCourseById_NotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            courseService.getCourseById(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Course not found with ID: 1", exception.getReason());
    }

    @Test
    void getCourseById_Error() {
        when(courseRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            courseService.getCourseById(1L);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error retrieving course", exception.getReason());
    }
}
