package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.CareerPath;
import com.CareerPathway.CareerPathway.model.CareerPathStep;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.repository.CareerPathRepository;
import com.CareerPathway.CareerPathway.repository.CareerPathStepRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CareerPathServiceImplTest {

    @Mock
    private CareerPathRepository careerPathRepository;

    @Mock
    private CareerPathStepRepository careerPathStepRepository;

    @InjectMocks
    private CareerPathServiceImpl careerPathService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCareerPath() {
        CareerPath careerPath = new CareerPath();
        when(careerPathRepository.save(any(CareerPath.class))).thenReturn(careerPath);

        CareerPath result = careerPathService.createCareerPath(careerPath);

        assertNotNull(result);
        verify(careerPathRepository, times(1)).save(careerPath);
    }

    @Test
    void getCareerPathsByEmployee() {
        User employee = new User();
        List<CareerPath> careerPaths = Arrays.asList(new CareerPath(), new CareerPath());
        when(careerPathRepository.findCareerPathByEmployee(employee)).thenReturn(careerPaths);

        List<CareerPath> result = careerPathService.getCareerPathsByEmployee(employee);

        assertEquals(2, result.size());
        verify(careerPathRepository, times(1)).findCareerPathByEmployee(employee);
    }

    @Test
    void getAllCareerPaths() {
        List<CareerPath> careerPaths = Arrays.asList(new CareerPath(), new CareerPath());
        when(careerPathRepository.findAll()).thenReturn(careerPaths);

        List<CareerPath> result = careerPathService.getAllCareerPaths();

        assertEquals(2, result.size());
        verify(careerPathRepository, times(1)).findAll();
    }

    @Test
    void updateCareerPathNotFound() {
        long careerPathId = 1L;
        CareerPath newCareerPath = new CareerPath();

        when(careerPathRepository.findById(careerPathId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            careerPathService.updateCareerPath(newCareerPath, careerPathId);
        });

        verify(careerPathRepository, times(1)).findById(careerPathId);
    }

    @Test
    void getCareerPathById() {
        long careerPathId = 1L;
        CareerPath careerPath = new CareerPath();
        when(careerPathRepository.findById(careerPathId)).thenReturn(Optional.of(careerPath));

        CareerPath result = careerPathService.getCareerPathById(careerPathId);

        assertNotNull(result);
        verify(careerPathRepository, times(1)).findById(careerPathId);
    }

    @Test
    void getCareerPathByIdNotFound() {
        long careerPathId = 1L;
        when(careerPathRepository.findById(careerPathId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            careerPathService.getCareerPathById(careerPathId);
        });

        verify(careerPathRepository, times(1)).findById(careerPathId);
    }

    @Test
    void deleteCareerPathById() {
        long careerPathId = 1L;
        CareerPath careerPath = new CareerPath();
        when(careerPathRepository.findById(careerPathId)).thenReturn(Optional.of(careerPath));

        boolean result = careerPathService.deleteCareerPathById(careerPathId);

        assertTrue(result);
        verify(careerPathRepository, times(1)).findById(careerPathId);
        verify(careerPathRepository, times(1)).delete(careerPath);
    }

    @Test
    void deleteCareerPathByIdNotFound() {
        long careerPathId = 1L;
        when(careerPathRepository.findById(careerPathId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            careerPathService.deleteCareerPathById(careerPathId);
        });

        verify(careerPathRepository, times(1)).findById(careerPathId);
    }

    @Test
    void updateCareerPathStep() {
        long stepId = 1L;
        CareerPathStep careerPathStep = new CareerPathStep();
        when(careerPathStepRepository.findById(stepId)).thenReturn(Optional.of(careerPathStep));
        when(careerPathStepRepository.save(any(CareerPathStep.class))).thenReturn(careerPathStep);

        CareerPathStep result = careerPathService.updateCareerPathStep(true, stepId);

        assertNotNull(result);
        assertTrue(result.isDone());
        verify(careerPathStepRepository, times(1)).findById(stepId);
        verify(careerPathStepRepository, times(1)).save(careerPathStep);
    }

    @Test
    void updateCareerPathStepNotFound() {
        long stepId = 1L;
        when(careerPathStepRepository.findById(stepId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            careerPathService.updateCareerPathStep(true, stepId);
        });

        verify(careerPathStepRepository, times(1)).findById(stepId);
    }

    @Test
    void updateCareerPathStatus() {
        long careerPathId = 1L;
        CareerPath careerPath = new CareerPath();
        when(careerPathRepository.findById(careerPathId)).thenReturn(Optional.of(careerPath));
        when(careerPathRepository.save(any(CareerPath.class))).thenReturn(careerPath);

        CareerPath result = careerPathService.updateCareerPathStatus(careerPathId);

        assertNotNull(result);
        assertTrue(result.isDone());
        verify(careerPathRepository, times(1)).findById(careerPathId);
        verify(careerPathRepository, times(1)).save(careerPath);
    }

    @Test
    void updateCareerPathStatusNotFound() {
        long careerPathId = 1L;
        when(careerPathRepository.findById(careerPathId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            careerPathService.updateCareerPathStatus(careerPathId);
        });

        verify(careerPathRepository, times(1)).findById(careerPathId);
    }
}
