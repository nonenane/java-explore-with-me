package ru.practicum.explorewithme.services.impl;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.exceptions.ConflictException;
import ru.practicum.explorewithme.exceptions.NotFoundException;
import ru.practicum.explorewithme.mappers.UserMapper;
import ru.practicum.explorewithme.models.User;
import ru.practicum.explorewithme.services.UserService;
import ru.practicum.explorewithme.storages.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.explorewithme.mappers.UserMapper.toUser;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto create(NewUserRequest newUserRequest) {
        try {
            User newUser = userRepository.save(toUser(newUserRequest));
            return UserMapper.toUserDto(newUser);
        } catch (ConstraintViolationException | DataIntegrityViolationException exp) {
            throw new ConflictException("Add User " + newUserRequest);
        }
    }

    @Override
    public List<UserDto> get(Set<Long> ids, Integer from, Integer size) {
        List<User> userList;
        if (ids == null || ids.isEmpty()) {
            userList = userRepository.findAllFromSize(from, size);
        } else {
            userList = userRepository.findAllByIdsFromSize(ids, from, size);
        }

        return userList.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found."));
        userRepository.deleteById(userId);
    }
}
