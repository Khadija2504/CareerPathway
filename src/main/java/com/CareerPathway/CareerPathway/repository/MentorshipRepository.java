package com.CareerPathway.CareerPathway.repository;

import com.CareerPathway.CareerPathway.model.Mentorship;
import com.CareerPathway.CareerPathway.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MentorshipRepository extends JpaRepository<Mentorship, Long> {
    List<Mentorship> findByMentorAndMentee(User mentor, User mentee);
    List<Mentorship> findByMentee(User mentee);
}
