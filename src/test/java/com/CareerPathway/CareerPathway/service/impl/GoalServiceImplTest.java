package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.EmployeeGoal;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.model.enums.GoalStatus;
import com.CareerPathway.CareerPathway.repository.GoalRepository;
import com.CareerPathway.CareerPathway.repository.NotificationRepository;
import com.CareerPathway.CareerPathway.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GoalServiceImplTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private GoalServiceImpl goalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private User createEmployee() {
        User employee = new User();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john@doe.com");
        employee.setPassword("password");
        return employee;
    }

    private EmployeeGoal createGoal() {
        EmployeeGoal goal = new EmployeeGoal();
        User employee = createEmployee();
        goal.setId(1L);
        goal.setGoalDescription("Learn Java");
        goal.setEmployee(employee);
        goal.setStatus(GoalStatus.IN_PROGRESS);
        goal.setTargetDate(LocalDate.now().plusDays(10));
        return goal;
    }

    @Test
    void addGoal_Success() {
        EmployeeGoal goal = createGoal();
        when(goalRepository.save(any(EmployeeGoal.class))).thenReturn(goal);

        EmployeeGoal result = goalService.addGoal(goal);

        assertNotNull(result);
        assertEquals("Learn Java", result.getGoalDescription());
        assertEquals(1L, result.getEmployee().getId());
        verify(goalRepository, times(1)).save(goal);
    }

    @Test
    void addGoal_NullEmployee() {
        EmployeeGoal goal = createGoal();
        goal.setEmployee(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            goalService.addGoal(goal);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Goal must be associated with a valid user", exception.getReason());
    }

    @Test
    void addGoal_Error() {
        EmployeeGoal goal = createGoal();
        when(goalRepository.save(any(EmployeeGoal.class))).thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            goalService.addGoal(goal);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error adding goal", exception.getReason());
    }

    @Test
    void getGoals_Success() {
        EmployeeGoal goal1 = createGoal();
        EmployeeGoal goal2 = createGoal();
        goal2.setId(2L);
        goal2.setGoalDescription("Learn Python");

        when(goalRepository.findEmployeeGoalByEmployeeId(1L)).thenReturn(Arrays.asList(goal1, goal2));

        List<EmployeeGoal> result = goalService.getGoals(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(goalRepository, times(1)).findEmployeeGoalByEmployeeId(1L);
    }

    @Test
    void getGoals_Error() {
        when(goalRepository.findEmployeeGoalByEmployeeId(1L)).thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            goalService.getGoals(1L);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error retrieving goals", exception.getReason());
    }

    @Test
    void updateGoalStatus_Success() {
        EmployeeGoal goal = createGoal();
        when(goalRepository.findEmployeeGoalById(1L)).thenReturn(Optional.of(goal));
        when(goalRepository.save(any(EmployeeGoal.class))).thenReturn(goal);

        EmployeeGoal result = goalService.updateGoalStatus(1L, "COMPLETED");

        assertNotNull(result);
        assertEquals(GoalStatus.COMPLETED, result.getStatus());
        verify(goalRepository, times(1)).findEmployeeGoalById(1L);
        verify(goalRepository, times(1)).save(goal);
    }

    @Test
    void updateGoalStatus_NotFound() {
        when(goalRepository.findEmployeeGoalById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            goalService.updateGoalStatus(1L, "COMPLETED");
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Goal not found with ID: 1", exception.getReason());
    }

    @Test
    void updateGoalStatus_Error() {
        EmployeeGoal goal = createGoal();
        when(goalRepository.findEmployeeGoalById(1L)).thenReturn(Optional.of(goal));
        when(goalRepository.save(any(EmployeeGoal.class))).thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            goalService.updateGoalStatus(1L, "COMPLETED");
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error updating goal status", exception.getReason());
    }

    @Test
    void deleteGoal_Success() {
        EmployeeGoal goal = createGoal();
        when(goalRepository.findEmployeeGoalById(1L)).thenReturn(Optional.of(goal));

        boolean result = goalService.deleteGoal(1L);

        assertTrue(result);
        verify(goalRepository, times(1)).delete(goal);
    }

    @Test
    void deleteGoal_NotFound() {
        when(goalRepository.findEmployeeGoalById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            goalService.deleteGoal(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Goal not found with ID: 1", exception.getReason());
    }

    @Test
    void deleteGoal_Error() {
        EmployeeGoal goal = createGoal();
        when(goalRepository.findEmployeeGoalById(1L)).thenReturn(Optional.of(goal));
        doThrow(new RuntimeException("Database error")).when(goalRepository).delete(goal);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            goalService.deleteGoal(1L);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error deleting goal", exception.getReason());
    }

}
