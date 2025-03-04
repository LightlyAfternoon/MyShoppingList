package ru.rsue.Karnaukhova.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.rsue.Karnaukhova.dto.ItemInListDTO;
import ru.rsue.Karnaukhova.entity.ItemInList;

import java.util.UUID;

@Mapper
public interface ItemInListDTOMapper {
    ItemInListDTOMapper INSTANCE = Mappers.getMapper(ItemInListDTOMapper.class);

    @Mapping(target = "uuid", ignore = true)
    ItemInList mapToEntity(ItemInListDTO itemInListDTO);

    @Mapping(target = "uuid", expression = "java(uuid)")
    ItemInList mapToEntity(ItemInListDTO itemInListDTO, UUID uuid);

    ItemInListDTO mapToDTO(ItemInList itemInList);
}