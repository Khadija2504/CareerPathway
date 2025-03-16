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
import io.micrometer.observation.annotation.Observed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        mentorship.setStatus(MentorshipStatus.Pending);
        return mentorshipRepository.save(mentorship);
    }

    @Override
    public boolean isMentorshipExist(User mentor, User mentee) {
        List<Mentorship> mentorship = mentorshipRepository.findByMentorAndMenteeAndStatus(mentor, mentee, MentorshipStatus.Active);
        return !mentorship.isEmpty();
    }

    @Override
    public List<Mentorship> getAllEmployeeMentorships(User mentee) {
        return mentorshipRepository.findByMentee(mentee);
    }

    @Override
    public List<Mentorship> getAllMentorMentorships(User mentor) {
        return mentorshipRepository.findByMentor(mentor);
    }

    @Override
    public Mentorship updateMentorshipStatus(MentorshipStatus status, long mentorshipId){
        Mentorship mentorship = mentorshipRepository.findById(mentorshipId).get();
        mentorship.setStatus(status);
        Notification notification = new Notification();
        notification.setRead(false);
        notification.setUser(mentorship.getMentee());
        String message = "";
        if(status == MentorshipStatus.Completed){
            message = "Mentor Mr." + mentorship.getMentor().getLastName() + " " + "have been closed this mentorship";
        } else if(status == MentorshipStatus.Active){
            message = "Mentor Mr." + mentorship.getMentor().getLastName() + " " + "have been active this mentorship";
        }
        notification.setMessage(message);
        notificationRepository.save(notification);
        return mentorshipRepository.save(mentorship);
    }

    @Override
    public List<Mentorship> getAllActiveMenteeMentorship(User mentee) {
        return mentorshipRepository.findByMenteeAndStatus(mentee, MentorshipStatus.Active);
    }

    @Override
    public MentorshipFeedback createFeedback(MentorshipFeedback feedback) {
        return mentorshipFeedbackRepository.save(feedback);
    }

    @Override
    public List<MentorshipFeedback> getAllMentorshipFeedbacks(long mentorshipId) {
        Mentorship mentorship = mentorshipRepository.findById(mentorshipId).get();
        return mentorshipFeedbackRepository.findByMentorship(mentorship);
    }
}
