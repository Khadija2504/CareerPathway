package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.dto.RegistrationDTO;
import com.CareerPathway.CareerPathway.model.Employee;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.model.enums.Role;
import com.CareerPathway.CareerPathway.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private RegistrationDTO createEmployee() {
        RegistrationDTO employee = new RegistrationDTO();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john@example.com");
        employee.setPassword("newPassword");
        employee.setRole(Role.EMPLOYEE);
        employee.setDepartment("It");
        employee.setJobTitle("Developer");
        return employee;
    }

    private User userUpdated() {
        User employee = new User();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john@example.com");
        employee.setPassword("password");
        employee.setRole(Role.EMPLOYEE);
        return employee;
    }

    @Test
    void testRegisterUser_ShouldRegisterEmployee() {
        RegistrationDTO registrationDTO = createEmployee();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        User mockUser = new Employee();
        mockUser.setEmail("john@example.com");
        mockUser.setRole(Role.EMPLOYEE);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User user = userService.registerUser(registrationDTO);

        assertNotNull(user);
        assertEquals("john@example.com", user.getEmail());
        assertEquals(Role.EMPLOYEE, user.getRole());
    }


    @Test
    void testRegisterUser_ShouldThrowDuplicateEmailException() {
        RegistrationDTO registrationDTO = createEmployee();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        assertThrows(ResponseStatusException.class, () -> userService.registerUser(registrationDTO));
    }

    @Test
    void testUpdateUserDetails_ShouldUpdateUser() {
        RegistrationDTO registrationDTO = createEmployee();
        User user = new Employee();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setPassword("password");
        user.setRole(Role.EMPLOYEE);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.updateUserDetails(1L, registrationDTO);

        assertNotNull(updatedUser);
        assertEquals("John", updatedUser.getFirstName());
        assertEquals("Doe", updatedUser.getLastName());
    }

    @Test
    void testUpdateUserDetails_ShouldThrowUserNotFoundException() {
        RegistrationDTO registrationDTO = createEmployee();

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.updateUserDetails(1L, registrationDTO));
    }

    @Test
    void testUserDetails_ShouldReturnUser() {
        User user = userUpdated();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.userDetails(1L);

        assertNotNull(foundUser);
        assertEquals("John", foundUser.getFirstName());
    }

    @Test
    void testUserDetails_ShouldThrowUserNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.userDetails(1L));
    }
}
