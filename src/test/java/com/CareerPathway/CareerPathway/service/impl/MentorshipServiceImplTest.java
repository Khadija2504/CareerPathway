package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.Mentorship;
import com.CareerPathway.CareerPathway.model.MentorshipFeedback;
import com.CareerPathway.CareerPathway.model.Notification;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.model.enums.MentorshipStatus;
import com.CareerPathway.CareerPathway.repository.MentorshipFeedbackRepository;
import com.CareerPathway.CareerPathway.repository.MentorshipRepository;
import com.CareerPathway.CareerPathway.repository.NotificationRepository;
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

class MentorshipServiceImplTest {

    @Mock
    private MentorshipRepository mentorshipRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private MentorshipFeedbackRepository mentorshipFeedbackRepository;

    @InjectMocks
    private MentorshipServiceImpl mentorshipService;

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

    private Mentorship createMentorship() {
        User mentor = createUser();
        User mentee = createUser();
        mentee.setId(2L);

        Mentorship mentorship = new Mentorship();
        mentorship.setId(1L);
        mentorship.setMentor(mentor);
        mentorship.setMentee(mentee);
        mentorship.setStatus(MentorshipStatus.Pending);
        return mentorship;
    }

    private MentorshipFeedback createFeedback() {
        MentorshipFeedback feedback = new MentorshipFeedback();
        feedback.setId(1L);
        feedback.setFeedback("Great mentorship!");
        feedback.setRating(5);
        return feedback;
    }

    @Test
    void save_Success() {
        Mentorship mentorship = createMentorship();
        when(mentorshipRepository.save(any(Mentorship.class))).thenReturn(mentorship);

        Mentorship result = mentorshipService.save(mentorship);

        assertNotNull(result);
        assertEquals(MentorshipStatus.Pending, result.getStatus());
        verify(mentorshipRepository, times(1)).save(mentorship);
    }

    @Test
    void save_NullMentorship() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mentorshipService.save(null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Mentorship, mentor, and mentee cannot be null", exception.getReason());
    }

    @Test
    void save_Error() {
        Mentorship mentorship = createMentorship();
        when(mentorshipRepository.save(any(Mentorship.class))).thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mentorshipService.save(mentorship);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error saving mentorship", exception.getReason());
    }

    @Test
    void isMentorshipExist_Success() {
        User mentor = createUser();
        User mentee = createUser();
        mentee.setId(2L);

        when(mentorshipRepository.findByMentorAndMenteeAndStatus(mentor, mentee, MentorshipStatus.Active))
                .thenReturn(Arrays.asList(createMentorship()));

        boolean result = mentorshipService.isMentorshipExist(mentor, mentee);

        assertTrue(result);
        verify(mentorshipRepository, times(1)).findByMentorAndMenteeAndStatus(mentor, mentee, MentorshipStatus.Active);
    }

    @Test
    void isMentorshipExist_NullMentorOrMentee() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mentorshipService.isMentorshipExist(null, createUser());
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Mentor and mentee cannot be null", exception.getReason());
    }

    @Test
    void isMentorshipExist_Error() {
        User mentor = createUser();
        User mentee = createUser();
        mentee.setId(2L);

        when(mentorshipRepository.findByMentorAndMenteeAndStatus(mentor, mentee, MentorshipStatus.Active))
                .thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mentorshipService.isMentorshipExist(mentor, mentee);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error checking mentorship existence", exception.getReason());
    }

    @Test
    void getAllEmployeeMentorships_Success() {
        User mentee = createUser();
        when(mentorshipRepository.findByMentee(mentee)).thenReturn(Arrays.asList(createMentorship()));

        List<Mentorship> result = mentorshipService.getAllEmployeeMentorships(mentee);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(mentorshipRepository, times(1)).findByMentee(mentee);
    }

    @Test
    void getAllEmployeeMentorships_NullMentee() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mentorshipService.getAllEmployeeMentorships(null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Mentee cannot be null", exception.getReason());
    }

    @Test
    void getAllEmployeeMentorships_Error() {
        User mentee = createUser();
        when(mentorshipRepository.findByMentee(mentee)).thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mentorshipService.getAllEmployeeMentorships(mentee);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error retrieving employee mentorships", exception.getReason());
    }

    @Test
    void getAllMentorMentorships_Success() {
        User mentor = createUser();
        when(mentorshipRepository.findByMentor(mentor)).thenReturn(Arrays.asList(createMentorship()));

        List<Mentorship> result = mentorshipService.getAllMentorMentorships(mentor);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(mentorshipRepository, times(1)).findByMentor(mentor);
    }

    @Test
    void getAllMentorMentorships_NullMentor() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mentorshipService.getAllMentorMentorships(null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Mentor cannot be null", exception.getReason());
    }

    @Test
    void getAllMentorMentorships_Error() {
        User mentor = createUser();
        when(mentorshipRepository.findByMentor(mentor)).thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mentorshipService.getAllMentorMentorships(mentor);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error retrieving mentor mentorships", exception.getReason());
    }

    @Test
    void updateMentorshipStatus_Success() {
        Mentorship mentorship = createMentorship();
        when(mentorshipRepository.findById(1L)).thenReturn(Optional.of(mentorship));
        when(mentorshipRepository.save(any(Mentorship.class))).thenReturn(mentorship);

        Mentorship result = mentorshipService.updateMentorshipStatus(MentorshipStatus.Active, 1L);

        assertNotNull(result);
        assertEquals(MentorshipStatus.Active, result.getStatus());
        verify(mentorshipRepository, times(1)).findById(1L);
        verify(mentorshipRepository, times(1)).save(mentorship);
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void updateMentorshipStatus_NotFound() {
        when(mentorshipRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mentorshipService.updateMentorshipStatus(MentorshipStatus.Active, 1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Mentorship not found with ID: 1", exception.getReason());
    }

    @Test
    void updateMentorshipStatus_Error() {
        Mentorship mentorship = createMentorship();
        when(mentorshipRepository.findById(1L)).thenReturn(Optional.of(mentorship));
        when(mentorshipRepository.save(any(Mentorship.class))).thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mentorshipService.updateMentorshipStatus(MentorshipStatus.Active, 1L);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error updating mentorship status", exception.getReason());
    }

    @Test
    void getAllActiveMenteeMentorship_Success() {
        User mentee = createUser();
        when(mentorshipRepository.findByMenteeAndStatus(mentee, MentorshipStatus.Active))
                .thenReturn(Arrays.asList(createMentorship()));

        List<Mentorship> result = mentorshipService.getAllActiveMenteeMentorship(mentee);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(mentorshipRepository, times(1)).findByMenteeAndStatus(mentee, MentorshipStatus.Active);
    }

    @Test
    void getAllActiveMenteeMentorship_NullMentee() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mentorshipService.getAllActiveMenteeMentorship(null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Mentee cannot be null", exception.getReason());
    }

    @Test
    void getAllActiveMenteeMentorship_Error() {
        User mentee = createUser();
        when(mentorshipRepository.findByMenteeAndStatus(mentee, MentorshipStatus.Active))
                .thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mentorshipService.getAllActiveMenteeMentorship(mentee);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error retrieving active mentorships for mentee", exception.getReason());
    }

    @Test
    void createFeedback_Success() {
        MentorshipFeedback feedback = createFeedback();
        Mentorship mentorship = createMentorship();
        when(mentorshipRepository.findById(1L)).thenReturn(Optional.of(mentorship));
        when(mentorshipFeedbackRepository.save(any(MentorshipFeedback.class))).thenReturn(feedback);

        MentorshipFeedback result = mentorshipService.createFeedback(feedback, 1L);

        assertNotNull(result);
        assertEquals("Great mentorship!", result.getFeedback());
        verify(mentorshipRepository, times(1)).findById(1L);
        verify(mentorshipFeedbackRepository, times(1)).save(feedback);
    }

    @Test
    void createFeedback_NullFeedback() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mentorshipService.createFeedback(null, 1L);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Feedback cannot be null", exception.getReason());
    }

    @Test
    void createFeedback_NotFound() {
        MentorshipFeedback feedback = createFeedback();
        when(mentorshipRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mentorshipService.createFeedback(feedback, 1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Mentorship not found with ID: 1", exception.getReason());
    }

    @Test
    void createFeedback_Error() {
        MentorshipFeedback feedback = createFeedback();
        Mentorship mentorship = createMentorship();
        when(mentorshipRepository.findById(1L)).thenReturn(Optional.of(mentorship));
        when(mentorshipFeedbackRepository.save(any(MentorshipFeedback.class))).thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mentorshipService.createFeedback(feedback, 1L);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error creating feedback", exception.getReason());
    }

    @Test
    void getAllMentorshipFeedbacks_Success() {
        Mentorship mentorship = createMentorship();
        when(mentorshipRepository.findById(1L)).thenReturn(Optional.of(mentorship));
        when(mentorshipFeedbackRepository.findByMentorship(mentorship)).thenReturn(Arrays.asList(createFeedback()));

        List<MentorshipFeedback> result = mentorshipService.getAllMentorshipFeedbacks(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(mentorshipRepository, times(1)).findById(1L);
        verify(mentorshipFeedbackRepository, times(1)).findByMentorship(mentorship);
    }

    @Test
    void getAllMentorshipFeedbacks_NotFound() {
        when(mentorshipRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mentorshipService.getAllMentorshipFeedbacks(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Mentorship not found with ID: 1", exception.getReason());
    }

    @Test
    void getAllMentorshipFeedbacks_Error() {
        Mentorship mentorship = createMentorship();
        when(mentorshipRepository.findById(1L)).thenReturn(Optional.of(mentorship));
        when(mentorshipFeedbackRepository.findByMentorship(mentorship)).thenThrow(new RuntimeException("Database error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            mentorshipService.getAllMentorshipFeedbacks(1L);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Error retrieving feedbacks", exception.getReason());
    }
}