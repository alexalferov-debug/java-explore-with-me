package ru.practicum.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.service.dto.user.NewUserRequest;
import ru.practicum.service.dto.user.UserDto;
import ru.practicum.service.model.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User toUser(NewUserRequest userCreate);

    UserDto toCreateUserResponse(User user);
}
