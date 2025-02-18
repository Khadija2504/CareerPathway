package com.CareerPathway.CareerPathway.mapper;

import com.CareerPathway.CareerPathway.dto.MentorshipDTO;
import com.CareerPathway.CareerPathway.model.Mentorship;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MentorshipMapper {
    Mentorship toEntity(MentorshipDTO mentorshipDTO);
    MentorshipDTO toDTO(Mentorship mentorship);
}
