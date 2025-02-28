package com.CareerPathway.CareerPathway.service;

import com.CareerPathway.CareerPathway.model.Mentorship;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.model.enums.MentorshipStatus;

import java.util.List;

public interface MentorshipService {
    Mentorship save(Mentorship mentorship);
    boolean isMentorshipExist(User mentor, User mentee);
    List<Mentorship> getAllEmployeeMentorships(User mentee);
    List<Mentorship> getAllMentorMentorships(User mentor);
    Mentorship updateMentorshipStatus(MentorshipStatus status, long mentorshipId);
}
