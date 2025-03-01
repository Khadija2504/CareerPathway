package com.CareerPathway.CareerPathway.mapper;

import com.CareerPathway.CareerPathway.dto.ResourceDTO;
import com.CareerPathway.CareerPathway.model.Resource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResourceMapper {
    Resource toEntity(ResourceDTO resourceDTO);
    ResourceDTO toDTO(Resource resource);

}
