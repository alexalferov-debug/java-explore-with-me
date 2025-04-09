package ru.practicum.service.logging;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.service.dto.user.NewUserRequest;
import ru.practicum.service.dto.user.UserDto;
import ru.practicum.service.service.user.UserService;

import java.util.List;

@Slf4j
public class LoggingUserDecorator implements UserService {
    private final UserService delegate;

    public LoggingUserDecorator(UserService delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        log.info("Запрос пользователей: ids={}, from={}, size={}", ids, from, size);
        return delegate.getUsers(ids, from, size);
    }

    @Override
    public UserDto create(NewUserRequest user) {
        log.info("Получен запрос на добавление пользователя с name = {}  email = {}", user.getName(), user.getEmail());
        return delegate.create(user);
    }

    @Override
    public UserDto get(long id) {
        log.info("Запрошена информация о пользователе с id = {}", id);
        return delegate.get(id);
    }

    @Override
    public void delete(long id) {
        log.info("Запрошено удаление пользователя с id = {}", id);
        delegate.delete(id);
    }
}