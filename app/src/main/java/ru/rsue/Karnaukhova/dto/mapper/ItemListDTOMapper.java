package ru.rsue.Karnaukhova.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.rsue.Karnaukhova.dto.ItemListDTO;
import ru.rsue.Karnaukhova.entity.ItemList;

import java.util.UUID;

@Mapper
public interface ItemListDTOMapper {
    ItemListDTOMapper INSTANCE = Mappers.getMapper(ItemListDTOMapper.class);

    @Mapping(target = "uuid", ignore = true)
    ItemList mapToEntity(ItemListDTO itemListDTO);

    @Mapping(target = "uuid", expression = "java(uuid)")
    ItemList mapToEntity(ItemListDTO itemListDTO, UUID uuid);

    ItemListDTO mapToDTO(ItemList itemList);
}