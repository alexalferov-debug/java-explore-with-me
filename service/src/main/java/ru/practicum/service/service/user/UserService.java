package ru.practicum.service.service.user;

import ru.practicum.service.dto.user.NewUserRequest;
import ru.practicum.service.dto.user.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(NewUserRequest user);

    UserDto get(long id);

    void delete(long id);

    List<UserDto> getUsers(List<Long> ids, int from, int size);
}
