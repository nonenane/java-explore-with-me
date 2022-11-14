package ru.practicum.explorewithme.mappers;

import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.dto.user.UserShortDto;
import ru.practicum.explorewithme.models.User;

public class UserMapper {

    public static UserShortDto toUserShortDto(User user) {
        if (user == null)
            return null;

        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static UserDto toUserDto(User user) {
        if (user == null)
            return null;

        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
