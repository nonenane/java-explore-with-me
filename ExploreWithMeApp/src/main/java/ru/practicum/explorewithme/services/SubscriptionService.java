package ru.practicum.explorewithme.services;

import ru.practicum.explorewithme.EventsSortType;
import ru.practicum.explorewithme.dto.event.EventShortDto;

import java.util.List;

public interface SubscriptionService {

    void createSubscription(Long userId, Long subsId);

    void deleteSubscription(Long userId, Long subsId);

    List<EventShortDto> getSubscriptionEvents(Long userId, EventsSortType sort, Integer from, Integer size);
}
