package com.CareerPathway.CareerPathway.mapper;

import com.CareerPathway.CareerPathway.dto.QuestionnaireDTO;
import com.CareerPathway.CareerPathway.model.Questionnaire;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionnaireMapper {
    Questionnaire toEntity(QuestionnaireDTO questionnaireDTO);
    QuestionnaireDTO toDTO(Questionnaire questionnaire);
}
