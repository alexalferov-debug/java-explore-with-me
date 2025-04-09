package ru.practicum.service.dao.user;

import org.springframework.data.domain.Pageable;
import ru.practicum.service.dto.user.NewUserRequest;
import ru.practicum.service.model.User;

import java.util.List;

public interface UserDao {
    User create(NewUserRequest user);

    void delete(Long id);

    User get(Long id);

    List<User> findByIdIn(List<Long> ids, Pageable pageable);

    List<User> getAll(Pageable pageable);
}
