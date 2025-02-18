package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.Mentorship;
import com.CareerPathway.CareerPathway.repository.MentorshipRepository;
import com.CareerPathway.CareerPathway.service.MentorshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MentorshipServiceImpl implements MentorshipService {
    @Autowired
    private MentorshipRepository mentorshipRepository;
    @Override
    public Mentorship save(Mentorship mentorship) {
        return mentorshipRepository.save(mentorship);
    }
}
