package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.Notification;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        return user;
    }

    private Notification createNotification() {
        Notification notification = new Notification();
        notification.setId(1L);
        notification.setUser(createUser());
        notification.setMessage("Test notification");
        notification.setSentAt(LocalDateTime.now());
        notification.setRead(false);
        return notification;
    }

    // Tests for getNotifications
    @Test
    void getNotifications_Success() {
        User user = createUser();
        Notification notification1 = createNotification();
        Notification notification2 = createNotification();
        notification2.setId(2L);
        notification2.setSentAt(LocalDateTime.now().minusDays(2));

        when(notificationRepository.findByUser(user)).thenReturn(Arrays.asList(notification1, notification2));

        List<Notification> result = notificationService.getNotifications(user);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(notificationRepository, times(1)).findByUser(user);
    }

    @Test
    void getNotifications_NullUser() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            notificationService.getNotifications(null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Employee cannot be null", exception.getReason());
    }

    @Test
    void getNotifications_Error() {
        User user = createUser();
        when(notificationRepository.findByUser(user)).thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            notificationService.getNotifications(user);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error retrieving notifications", exception.getReason());
    }

    @Test
    void readNotifications_Success() {
        User user = createUser();
        Notification notification1 = createNotification();
        Notification notification2 = createNotification();
        notification2.setId(2L);

        when(notificationRepository.findByUserAndRead(user, false)).thenReturn(Arrays.asList(notification1, notification2));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification1);

        List<Notification> result = notificationService.readNotifications(user);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(notificationRepository, times(1)).findByUserAndRead(user, false);
        verify(notificationRepository, times(2)).save(any(Notification.class));
    }

    @Test
    void readNotifications_NullUser() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            notificationService.readNotifications(null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Employee cannot be null", exception.getReason());
    }

    @Test
    void readNotifications_Error() {
        User user = createUser();
        when(notificationRepository.findByUserAndRead(user, false)).thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            notificationService.readNotifications(user);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error marking notifications as read", exception.getReason());
    }

    @Test
    void unreadNotifications_Success() {
        User user = createUser();
        Notification notification1 = createNotification();
        Notification notification2 = createNotification();
        notification2.setId(2L);

        when(notificationRepository.findByUserAndRead(user, false)).thenReturn(Arrays.asList(notification1, notification2));

        List<Notification> result = notificationService.unreadNotifications(user);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(notificationRepository, times(1)).findByUserAndRead(user, false);
    }

    @Test
    void unreadNotifications_NullUser() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            notificationService.unreadNotifications(null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Employee cannot be null", exception.getReason());
    }

    @Test
    void unreadNotifications_Error() {
        User user = createUser();
        when(notificationRepository.findByUserAndRead(user, false)).thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            notificationService.unreadNotifications(user);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error retrieving unread notifications", exception.getReason());
    }

    @Test
    void createNotification_Success() {
        User user = createUser();
        Notification notification = createNotification();

        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification result = notificationService.createNotification("Test notification", user);

        assertNotNull(result);
        assertEquals("Test notification", result.getMessage());
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void createNotification_NullMessageOrUser() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            notificationService.createNotification(null, createUser());
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Message and user cannot be null", exception.getReason());
    }

    @Test
    void createNotification_Error() {
        User user = createUser();
        when(notificationRepository.save(any(Notification.class))).thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            notificationService.createNotification("Test notification", user);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error creating notification", exception.getReason());
    }
}