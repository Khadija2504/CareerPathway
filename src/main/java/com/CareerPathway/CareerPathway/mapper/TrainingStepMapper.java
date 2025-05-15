package com.CareerPathway.CareerPathway.mapper;

import com.CareerPathway.CareerPathway.dto.TrainingStepDTO;
import com.CareerPathway.CareerPathway.model.TrainingStep;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainingStepMapper {
    TrainingStepDTO toDto(TrainingStep trainingStep);
    TrainingStep toEntity(TrainingStepDTO dto);
}

