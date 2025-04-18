package ru.practicum.service.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.dao.user.UserDao;
import ru.practicum.service.dto.user.NewUserRequest;
import ru.practicum.service.dto.user.UserDto;
import ru.practicum.service.mapper.UserMapper;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userRepository) {
        this.userDao = userRepository;
    }

    @Override
    @Transactional
    public UserDto create(NewUserRequest user) {
        return UserMapper.INSTANCE.toCreateUserResponse(userDao.create(user));
    }

    @Override
    @Transactional
    public void delete(long id) {
        userDao.delete(id);
    }

    @Override
    public UserDto get(long id) {
        return UserMapper.INSTANCE.toCreateUserResponse(userDao.get(id));
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (ids != null && !ids.isEmpty()) {
            return userDao.findByIdIn(ids, pageable).stream().map(UserMapper.INSTANCE::toCreateUserResponse).toList();
        } else {
            return userDao.getAll(pageable).stream().map(UserMapper.INSTANCE::toCreateUserResponse).toList();
        }
    }
}
