package ru.practicum.explorewithme.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.EventsSortType;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.exceptions.ConflictException;
import ru.practicum.explorewithme.exceptions.NotFoundException;
import ru.practicum.explorewithme.mappers.EventMapper;
import ru.practicum.explorewithme.models.Event;
import ru.practicum.explorewithme.models.User;
import ru.practicum.explorewithme.services.SubscriptionService;
import ru.practicum.explorewithme.storages.UserRepository;
import ru.practicum.explorewithme.storages.event.EventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public SubscriptionServiceImpl(UserRepository userRepository, EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public void create(Long userId, Long subsId) {
        getUserFromDB(subsId);
        if (userRepository.checkSubscription(userId, subsId) == 1) {
            throw new ConflictException("Subscription already exist");
        }
        userRepository.createSubscription(userId, subsId);
    }

    @Override
    @Transactional
    public void delete(Long userId, Long subsId) {
        getUserFromDB(subsId);
        if (userRepository.checkSubscription(userId, subsId) == 0) {
            throw new ConflictException("Subscription does not exist");
        }
        userRepository.deleteSubscription(userId, subsId);
    }

    @Override
    public List<EventShortDto> get(Long userId, EventsSortType sort, Integer from, Integer size) {
        List<Long> subscriptionsIds = userRepository.findSubscriptionsIds(userId);
        if (subscriptionsIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Event> eventList = eventRepository.findSubscriptionsEvents(subscriptionsIds, sort, from, size);

        return eventList.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    private User getUserFromDB(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found."));
    }
}
