package ru.rsue.Karnaukhova.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.rsue.Karnaukhova.dto.WeightUnitDTO;
import ru.rsue.Karnaukhova.entity.WeightUnit;

import java.util.UUID;

@Mapper
public interface WeightUnitDTOMapper {
    WeightUnitDTOMapper INSTANCE = Mappers.getMapper(WeightUnitDTOMapper.class);

    @Mapping(target = "uuid", ignore = true)
    WeightUnit mapToEntity(WeightUnitDTO weightUnitDTO);

    @Mapping(target = "uuid", expression = "java(uuid)")
    WeightUnit mapToEntity(WeightUnitDTO weightUnitDTO, UUID uuid);

    WeightUnitDTO mapToDTO(WeightUnit weightUnit);
}