package com.CareerPathway.CareerPathway.mapper;

import com.CareerPathway.CareerPathway.dto.MessageDTO;
import com.CareerPathway.CareerPathway.model.Message;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    Message toEntity(MessageDTO messageDTO);
    MessageDTO toDTO(Message message);
}
