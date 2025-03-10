package com.CareerPathway.CareerPathway.mapper;

import com.CareerPathway.CareerPathway.dto.MentorshipFeedbackDTO;
import com.CareerPathway.CareerPathway.model.MentorshipFeedback;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MentorshipFeedBackMapper {
MentorshipFeedback toEntity(MentorshipFeedbackDTO mentorshipFeedbackDTO);
MentorshipFeedbackDTO toDTO(MentorshipFeedback mentorshipFeedback);
}
