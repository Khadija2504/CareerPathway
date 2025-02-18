package com.CareerPathway.CareerPathway.service;

import com.CareerPathway.CareerPathway.model.Mentorship;
import com.CareerPathway.CareerPathway.model.User;

import java.util.List;

public interface MentorshipService {
    Mentorship save(Mentorship mentorship);
    boolean isMentorshipExist(User mentor, User mentee);
}
