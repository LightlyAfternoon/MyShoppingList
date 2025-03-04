package ru.rsue.Karnaukhova.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.rsue.Karnaukhova.dto.ItemDTO;
import ru.rsue.Karnaukhova.entity.Item;

import java.util.UUID;

@Mapper
public interface ItemDTOMapper {
    ItemDTOMapper INSTANCE = Mappers.getMapper(ItemDTOMapper.class);

    @Mapping(target = "uuid", ignore = true)
    Item mapToEntity(ItemDTO itemDTO);

    @Mapping(target = "uuid", expression = "java(uuid)")
    Item mapToEntity(ItemDTO itemDTO, UUID uuid);

    ItemDTO mapToDTO(Item item);
}