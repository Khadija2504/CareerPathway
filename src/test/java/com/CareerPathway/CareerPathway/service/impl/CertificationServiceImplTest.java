package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.CareerPath;
import com.CareerPathway.CareerPathway.model.Certification;
import com.CareerPathway.CareerPathway.model.Notification;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.model.enums.Role;
import com.CareerPathway.CareerPathway.repository.CertificationRepository;
import com.CareerPathway.CareerPathway.repository.NotificationRepository;
import com.CareerPathway.CareerPathway.repository.UserRepository;
import com.CareerPathway.CareerPathway.util.PdfCertificateGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CertificationServiceImplTest {

    @Mock
    private CertificationRepository certificationRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PdfCertificateGenerator pdfCertificateGenerator;

    @InjectMocks
    private CertificationServiceImpl certificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private CareerPath createCareerPath() {
        CareerPath careerPath = new CareerPath();
        careerPath.setId(1L);
        careerPath.setName("Java Developer");
        return careerPath;
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        return user;
    }

    private Certification createCertification(CareerPath careerPath, User user) {
        return Certification.builder()
                .user(user)
                .careerPath(careerPath)
                .certificateUrl("http://localhost:8800/uploads/certificate_Java_Developer_1_1.pdf")
                .build();
    }

    @Test
    void generateCertification_Success() {
        CareerPath careerPath = createCareerPath();
        User employee = createUser();
        User admin = new User();
        admin.setRole(Role.ADMIN);

        when(userRepository.findByRole(Role.ADMIN)).thenReturn(admin);
        when(certificationRepository.save(any(Certification.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(notificationRepository.save(any(Notification.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Certification result = certificationService.generateCertification(careerPath, employee);

        assertNotNull(result);
        assertEquals("http://localhost:8800/uploads/certificate_Java_Developer_1_1.pdf", result.getCertificateUrl());
        verify(certificationRepository, times(1)).save(any(Certification.class));
        verify(notificationRepository, times(1)).save(any(Notification.class));
        verify(userRepository, times(1)).findByRole(Role.ADMIN);
    }

    @Test
    void generateCertification_NullCareerPath() {
        User user = createUser();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            certificationService.generateCertification(null, user);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Career path or employee cannot be null", exception.getReason());
    }

    @Test
    void generateCertification_NullEmployee() {
        CareerPath careerPath = createCareerPath();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            certificationService.generateCertification(careerPath, null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Career path or employee cannot be null", exception.getReason());
    }

    @Test
    void generateCertification_AdminNotFound() {
        CareerPath careerPath = createCareerPath();
        User employee = createUser();

        when(userRepository.findByRole(Role.ADMIN)).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            certificationService.generateCertification(careerPath, employee);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("admin can not be null", exception.getReason());
    }

    @Test
    void generateCertification_SaveFailure() {
        CareerPath careerPath = createCareerPath();
        User employee = createUser();
        User admin = new User();
        admin.setRole(Role.ADMIN);

        when(userRepository.findByRole(Role.ADMIN)).thenReturn(admin);
        when(certificationRepository.save(any(Certification.class))).thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            certificationService.generateCertification(careerPath, employee);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Failed to save certification", exception.getReason());
    }

    @Test
    void getCertification_Success() {
        CareerPath careerPath = createCareerPath();
        User user = createUser();
        Certification certification = createCertification(careerPath, user);

        when(certificationRepository.findCertificationByCareerPath(careerPath)).thenReturn(certification);

        Certification result = certificationService.getCertification(careerPath);

        assertNotNull(result);
        assertEquals("http://localhost:8800/uploads/certificate_Java_Developer_1_1.pdf", result.getCertificateUrl());
        verify(certificationRepository, times(1)).findCertificationByCareerPath(careerPath);
    }

    @Test
    void getCertification_NotFound() {
        CareerPath careerPath = createCareerPath();

        when(certificationRepository.findCertificationByCareerPath(careerPath)).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            certificationService.getCertification(careerPath);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Certification not found for the given career path", exception.getReason());
    }

    @Test
    void getCertification_NullCareerPath() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            certificationService.getCertification(null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Career path cannot be null", exception.getReason());
    }

    @Test
    void getCertification_RepositoryError() {
        CareerPath careerPath = createCareerPath();

        when(certificationRepository.findCertificationByCareerPath(careerPath)).thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            certificationService.getCertification(careerPath);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error retrieving certification", exception.getReason());
    }
}
