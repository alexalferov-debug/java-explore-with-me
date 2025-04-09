package ru.practicum.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.service.dto.location.Location;

@Mapper
public interface LocationMapper {

    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    @Mapping(target = "lat", source = "latitude")
    @Mapping(target = "lon", source = "longitude")
    Location toDto(ru.practicum.service.model.Location location);

    @Mapping(target = "latitude", source = "lat")
    @Mapping(target = "longitude", source = "lon")
    ru.practicum.service.model.Location toEntity(Location location);
}
