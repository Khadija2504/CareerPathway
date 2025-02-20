package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.Mentorship;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.repository.MentorshipRepository;
import com.CareerPathway.CareerPathway.service.MentorshipService;
import io.micrometer.observation.annotation.Observed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MentorshipServiceImpl implements MentorshipService {
    @Autowired
    private MentorshipRepository mentorshipRepository;
    @Override
    public Mentorship save(Mentorship mentorship) {
        return mentorshipRepository.save(mentorship);
    }

    @Override
    public boolean isMentorshipExist(User mentor, User mentee) {
        List<Mentorship> mentorship = mentorshipRepository.findByMentorAndMentee(mentor, mentee);
        return !mentorship.isEmpty();
    }

    @Override
    public List<Mentorship> getAllMentorships(User mentee) {
        return mentorshipRepository.findByMentee(mentee);
    }
}
