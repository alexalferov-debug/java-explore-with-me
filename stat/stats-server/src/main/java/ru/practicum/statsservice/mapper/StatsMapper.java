package ru.practicum.statsservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.statscommon.dto.EndpointHit;
import ru.practicum.statsservice.model.StatsData;

@Mapper
public interface StatsMapper {
    StatsMapper INSTANCE = Mappers.getMapper(StatsMapper.class);

    @Mapping(target = "app", source = "app")
    @Mapping(target = "uri", source = "uri")
    @Mapping(target = "ip", source = "ip")
    @Mapping(target = "timestamp", source = "timestamp")
    StatsData fromDto(EndpointHit endpointHit);
}
