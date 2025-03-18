package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.dto.ProgressMetricsDTO;
import com.CareerPathway.CareerPathway.model.*;
import com.CareerPathway.CareerPathway.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgressServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SkillAssessmentRepository skillAssessmentRepository;

    @Mock
    private CareerPathRepository careerPathRepository;

    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private GoalRepository employeeGoalRepository;

    @InjectMocks
    private ProgressServiceImpl progressService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
    }

    @Test
    void testCalculateProgressMetrics_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(skillAssessmentRepository.findByUserId(1L)).thenReturn(Collections.emptyList());
        when(careerPathRepository.findCareerPathByEmployee(user)).thenReturn(Collections.emptyList());
        when(trainingRepository.findByUser(user)).thenReturn(Collections.emptyList());
        when(employeeGoalRepository.findEmployeeGoalByEmployeeId(1L)).thenReturn(Collections.emptyList());

        ProgressMetricsDTO result = progressService.calculateProgressMetrics(1L);

        assertEquals(0, result.getSkillAssessmentProgress());
        assertEquals(0, result.getCareerPathProgress());
        assertEquals(0, result.getTrainingProgress());
        assertEquals(0, result.getGoalProgress());
    }

    @Test
    void testCalculateProgressMetrics_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> progressService.calculateProgressMetrics(1L));
    }
}
