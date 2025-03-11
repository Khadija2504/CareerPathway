package com.CareerPathway.CareerPathway.mapper;

import com.CareerPathway.CareerPathway.dto.SkillDTO;
import com.CareerPathway.CareerPathway.model.Skill;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SkillMapper {
    Skill toEntity(SkillDTO skillDTO);
    SkillDTO toDTO(Skill skill);
}
