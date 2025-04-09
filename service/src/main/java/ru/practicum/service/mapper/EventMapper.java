package ru.practicum.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.service.dto.event.EventFullDto;
import ru.practicum.service.dto.event.EventShortDto;
import ru.practicum.service.dto.event.NewEventDto;
import ru.practicum.service.model.Category;
import ru.practicum.service.model.Event;

@Mapper(uses = {LocationMapper.class, CategoryMapper.class, UserMapper.class})
public interface EventMapper {
    EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

    @Mapping(target = "category", expression = "java(mapCategory(newEventDto.getCategory()))")
    Event fromDto(NewEventDto newEventDto);

    EventFullDto toFullDto(Event event);

    EventShortDto toShortDto(Event event);

    default Category mapCategory(Long id) {
        Category category = new Category();
        category.setId(id);
        return category;
    }
}
