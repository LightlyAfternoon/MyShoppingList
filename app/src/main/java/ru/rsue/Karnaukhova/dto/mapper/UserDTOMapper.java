package ru.rsue.Karnaukhova.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.rsue.Karnaukhova.dto.UserDTO;
import ru.rsue.Karnaukhova.entity.User;

import java.util.UUID;

@Mapper
public interface UserDTOMapper {
    UserDTOMapper INSTANCE = Mappers.getMapper(UserDTOMapper.class);

    @Mapping(target = "uuid", ignore = true)
    User mapToEntity(UserDTO userDTO);

    @Mapping(target = "uuid", expression = "java(uuid)")
    User mapToEntity(UserDTO userDTO, UUID uuid);

    UserDTO mapToDTO(User user);
}