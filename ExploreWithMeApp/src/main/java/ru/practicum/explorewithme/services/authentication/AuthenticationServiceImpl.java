package ru.practicum.explorewithme.services.authentication;

import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.exceptions.NotFoundException;
import ru.practicum.explorewithme.storages.UserRepository;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    public AuthenticationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public boolean checkUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found."));
        return true;
    }
}
