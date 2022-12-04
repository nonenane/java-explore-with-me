package ru.practicum.explorewithme.services.authentication;

public interface AuthenticationService {

    void throwIfUserNotFound(Long userId);
}
