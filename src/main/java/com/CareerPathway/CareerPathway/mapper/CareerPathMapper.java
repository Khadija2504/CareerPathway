package com.CareerPathway.CareerPathway.mapper;

import com.CareerPathway.CareerPathway.dto.CareerPathDTO;
import com.CareerPathway.CareerPathway.model.CareerPath;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CareerPathMapper {
    CareerPath toEntity(CareerPathDTO careerPathDTO);
    CareerPathDTO toDTO(CareerPath careerPath);
}
