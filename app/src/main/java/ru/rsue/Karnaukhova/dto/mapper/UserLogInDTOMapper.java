package ru.rsue.Karnaukhova.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.rsue.Karnaukhova.dto.UserLogInDTO;
import ru.rsue.Karnaukhova.entity.User;

@Mapper
public interface UserLogInDTOMapper {
    UserLogInDTOMapper INSTANCE = Mappers.getMapper(UserLogInDTOMapper.class);

    User mapToEntity(UserLogInDTO userDTO);

    UserLogInDTO mapToDTO(User user);
}