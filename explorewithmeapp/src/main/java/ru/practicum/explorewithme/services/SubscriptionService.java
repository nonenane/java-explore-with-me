package ru.practicum.explorewithme.services;

import ru.practicum.explorewithme.EventsSortType;
import ru.practicum.explorewithme.dto.event.EventShortDto;

import java.util.List;

public interface SubscriptionService {

    void create(Long userId, Long subsId);

    void delete(Long userId, Long subsId);

    List<EventShortDto> get(Long userId, EventsSortType sort, Integer from, Integer size);
}
