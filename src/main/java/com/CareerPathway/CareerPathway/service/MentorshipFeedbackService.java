package com.CareerPathway.CareerPathway.service;

import com.CareerPathway.CareerPathway.model.Mentorship;
import com.CareerPathway.CareerPathway.model.MentorshipFeedback;

import java.util.List;

public interface MentorshipFeedbackService {
    MentorshipFeedback createFeedback(MentorshipFeedback feedback);
    List<MentorshipFeedback> getAllMentorshipFeedbacks(long mentorshipId);
}
