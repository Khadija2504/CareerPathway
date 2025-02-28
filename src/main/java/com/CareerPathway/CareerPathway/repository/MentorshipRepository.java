package com.CareerPathway.CareerPathway.repository;

import com.CareerPathway.CareerPathway.model.Mentorship;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.model.enums.MentorshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentorshipRepository extends JpaRepository<Mentorship, Long> {
    List<Mentorship> findByMentorAndMenteeAndStatus(User mentor, User mentee, MentorshipStatus status);
    List<Mentorship> findByMentee(User mentee);
    List<Mentorship> findByMentor(User mentor);
}
