package ru.practicum.explorewithme.services;

import ru.practicum.explorewithme.dto.event.*;
import ru.practicum.explorewithme.storages.event.EventSearchParamsModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    List<EventShortDto> getAll(EventSearchParamsModel paramModel, HttpServletRequest request);

    List<EventFullDto> getEventsAdmin(EventSearchParamsModel paramModel);


    EventFullDto get(Long eventId, HttpServletRequest request);

    EventFullDto getMyEvent(Long userId, Long eventId);

    List<EventShortDto> getMyEvents(Long userId, Integer from, Integer size);

    EventFullDto create(NewEventDto newEventDto, Long userId);

    EventFullDto patch(UpdateEventRequest updateEventRequest);

    EventFullDto adminUpdateEvent(AdminUpdateEventRequest updateEventRequest, Long eventId);

    EventFullDto cancelEvent(Long userId, Long eventId);

    EventFullDto publishEvent(Long eventId);

    EventFullDto rejectEvent(Long eventId);

    /**
     * Увеличивает или умееньшает значение подтвержденных заявок на участие в событии на 1
     */
    void changeConfirmRequests(boolean increase, Long eventId);
}
