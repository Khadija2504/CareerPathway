package com.CareerPathway.CareerPathway.mapper;

import com.CareerPathway.CareerPathway.dto.CareerPathStepDTO;
import com.CareerPathway.CareerPathway.model.CareerPathStep;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CareerPathStepMapper {
    List<CareerPathStep> toEntity(List<CareerPathStepDTO> dtos);
    CareerPathStepDTO toDTO(CareerPathStep careerPathStep);
}
