package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.Mentorship;
import com.CareerPathway.CareerPathway.model.MentorshipFeedback;
import com.CareerPathway.CareerPathway.model.Notification;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.model.enums.MentorshipStatus;
import com.CareerPathway.CareerPathway.repository.MentorshipFeedbackRepository;
import com.CareerPathway.CareerPathway.repository.MentorshipRepository;
import com.CareerPathway.CareerPathway.repository.NotificationRepository;
import com.CareerPathway.CareerPathway.service.MentorshipFeedbackService;
import com.CareerPathway.CareerPathway.service.MentorshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MentorshipServiceImpl implements MentorshipService, MentorshipFeedbackService {
    @Autowired
    private MentorshipRepository mentorshipRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private MentorshipFeedbackRepository mentorshipFeedbackRepository;

    @Override
    public Mentorship save(Mentorship mentorship) {
        if (mentorship == null || mentorship.getMentor() == null || mentorship.getMentee() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mentorship, mentor, and mentee cannot be null");
        }

        String message = "U have new connection request from " + mentorship.getMentee() +", check it in mentorship list now";

        try {
            Notification notification = new Notification();
            notification.setRead(false);
            notification.setUser(mentorship.getMentor());
            notification.setMessage(message);
            notification.setSentAt(LocalDateTime.now());
            notificationRepository.save(notification);
            mentorship.setStatus(MentorshipStatus.Pending);
            return mentorshipRepository.save(mentorship);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error saving mentorship", e);
        }
    }

    @Override
    public boolean isMentorshipExist(User mentor, User mentee) {
        if (mentor == null || mentee == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mentor and mentee cannot be null");
        }

        try {
            List<Mentorship> mentorship = mentorshipRepository.findByMentorAndMenteeAndStatus(mentor, mentee, MentorshipStatus.Active);
            return !mentorship.isEmpty();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error checking mentorship existence", e);
        }
    }

    @Override
    public List<Mentorship> getAllEmployeeMentorships(User mentee) {
        if (mentee == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mentee cannot be null");
        }

        try {
            return mentorshipRepository.findByMentee(mentee);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving employee mentorships", e);
        }
    }

    @Override
    public List<Mentorship> getAllMentorMentorships(User mentor) {
        if (mentor == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mentor cannot be null");
        }

        try {
            return mentorshipRepository.findByMentor(mentor);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving mentor mentorships", e);
        }
    }

    @Override
    public Mentorship updateMentorshipStatus(MentorshipStatus status, long mentorshipId) {
        try {
            Optional<Mentorship> mentorshipOpt = mentorshipRepository.findById(mentorshipId);
            if (mentorshipOpt.isPresent()) {
                Mentorship mentorship = mentorshipOpt.get();
                mentorship.setStatus(status);

                Notification notification = new Notification();
                notification.setRead(false);
                notification.setUser(mentorship.getMentee());
                String message = "";
                if (status == MentorshipStatus.Completed) {
                    message = "Mentor Mr." + mentorship.getMentor().getLastName() + " has closed this mentorship";
                } else if (status == MentorshipStatus.Active) {
                    message = "Mentor Mr." + mentorship.getMentor().getLastName() + " has activated this mentorship";
                }
                notification.setMessage(message);
                notificationRepository.save(notification);

                return mentorshipRepository.save(mentorship);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentorship not found with ID: " + mentorshipId);
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating mentorship status", e);
        }
    }

    @Override
    public List<Mentorship> getAllActiveMenteeMentorship(User mentee) {
        if (mentee == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mentee cannot be null");
        }

        try {
            return mentorshipRepository.findByMenteeAndStatus(mentee, MentorshipStatus.Active);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving active mentorships for mentee", e);
        }
    }

    @Override
    public MentorshipFeedback createFeedback(MentorshipFeedback feedback, long mentorshipId) {
        if (feedback == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Feedback cannot be null");
        }

        try {
            Optional<Mentorship> mentorshipOpt = mentorshipRepository.findById(mentorshipId);
            if (mentorshipOpt.isPresent()) {
                Mentorship mentorship = mentorshipOpt.get();
                String message = "U have new feedback from " + mentorship.getMentee().getFirstName() + " " + mentorship.getMentee().getLastName();
                feedback.setMentorship(mentorship);
                Notification notification = new Notification();
                notification.setRead(false);
                notification.setUser(mentorship.getMentor());
                notification.setMessage(message);
                notification.setSentAt(LocalDateTime.now());
                notificationRepository.save(notification);
                return mentorshipFeedbackRepository.save(feedback);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentorship not found with ID: " + mentorshipId);
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating feedback", e);
        }
    }

    @Override
    public List<MentorshipFeedback> getAllMentorshipFeedbacks(long mentorshipId) {
        try {
            Optional<Mentorship> mentorshipOpt = mentorshipRepository.findById(mentorshipId);
            if (mentorshipOpt.isPresent()) {
                Mentorship mentorship = mentorshipOpt.get();
                return mentorshipFeedbackRepository.findByMentorship(mentorship);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mentorship not found with ID: " + mentorshipId);
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving feedbacks", e);
        }
    }
}
