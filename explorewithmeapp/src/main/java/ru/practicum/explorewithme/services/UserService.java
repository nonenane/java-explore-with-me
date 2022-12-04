package ru.practicum.explorewithme.services;

import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserDto create(NewUserRequest newUserRequest);

    List<UserDto> get(Set<Long> users, Integer from, Integer size);

    void delete(Long userId);
}
