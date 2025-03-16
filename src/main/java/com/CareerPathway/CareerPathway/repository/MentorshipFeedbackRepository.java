package com.CareerPathway.CareerPathway.repository;

import com.CareerPathway.CareerPathway.model.Mentor;
import com.CareerPathway.CareerPathway.model.Mentorship;
import com.CareerPathway.CareerPathway.model.MentorshipFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentorshipFeedbackRepository extends JpaRepository<MentorshipFeedback, Long> {
    List<MentorshipFeedback> findByMentorship(Mentorship mentorship);
}
