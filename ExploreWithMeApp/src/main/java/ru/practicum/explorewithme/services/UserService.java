package ru.practicum.explorewithme.services;

import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserDto createUser(NewUserRequest newUserRequest);

    List<UserDto> getUsers(Set<Long> users, Integer from, Integer size);

    void deleteUser(Long userId);
}
