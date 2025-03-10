package com.CareerPathway.CareerPathway.repository;

import com.CareerPathway.CareerPathway.model.MentorshipFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentorshipFeedbackRepository extends JpaRepository<MentorshipFeedback, Long> {
}
