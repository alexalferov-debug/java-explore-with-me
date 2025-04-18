package ru.practicum.service.dao.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.dto.user.NewUserRequest;
import ru.practicum.service.exception.NotFoundException;
import ru.practicum.service.mapper.UserMapper;
import ru.practicum.service.model.User;
import ru.practicum.service.repository.UserRepository;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class UserDaoImpl implements UserDao {
    UserRepository userRepository;

    @Autowired
    public UserDaoImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User create(NewUserRequest user) {
        return userRepository.save(UserMapper.INSTANCE.toUser(user));
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public User get(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь c id = " + id + " не найден"));
    }

    public List<User> findByIdIn(List<Long> ids, Pageable pageable) {
        return userRepository.findByIdIn(ids, pageable).getContent();
    }

    public List<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable).getContent();
    }
}
