package com.CareerPathway.CareerPathway.mapper;

import com.CareerPathway.CareerPathway.dto.UserDTO;
import com.CareerPathway.CareerPathway.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDTO userDTO);
    UserDTO toDTO(User user);
}
