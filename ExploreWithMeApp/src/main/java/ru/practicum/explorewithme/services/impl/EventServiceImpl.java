package ru.practicum.explorewithme.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.EventState;
import ru.practicum.explorewithme.dto.event.*;
import ru.practicum.explorewithme.dto.statistic.EndpointHitDto;
import ru.practicum.explorewithme.exceptions.BadRequestException;
import ru.practicum.explorewithme.exceptions.ForbiddenException;
import ru.practicum.explorewithme.exceptions.NotFoundException;
import ru.practicum.explorewithme.mappers.EventMapper;
import ru.practicum.explorewithme.models.Category;
import ru.practicum.explorewithme.models.Event;
import ru.practicum.explorewithme.models.User;
import ru.practicum.explorewithme.services.EventService;
import ru.practicum.explorewithme.services.statclient.StatClient;
import ru.practicum.explorewithme.storages.CategoryRepository;
import ru.practicum.explorewithme.storages.UserRepository;
import ru.practicum.explorewithme.storages.event.EventRepository;
import ru.practicum.explorewithme.storages.event.EventSearchParamsModel;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatClient statClient;

    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository, CategoryRepository categoryRepository, StatClient statClient) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.statClient = statClient;
    }

    @Override
    public List<EventShortDto> getEvents(EventSearchParamsModel paramModel,
                                         HttpServletRequest request) {

        List<Event> eventList = eventRepository.publicSearchByParameters(paramModel);

        statClient.createEndpointHit(new EndpointHitDto(null,
                "ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()));

        return eventList.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> getEventsAdmin(EventSearchParamsModel paramModel) {

        List<Event> eventList = eventRepository.adminSearchByParameters(paramModel);

        return eventList.stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEvent(Long eventId, HttpServletRequest request) {
        Event event = getEventFromDB(eventId);

        statClient.createEndpointHit(new EndpointHitDto(null,
                "ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()));

        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto getMyEvent(Long userId, Long eventId) {
        Event event = getEventFromDB(eventId);
        if (!Objects.equals(event.getInitiator().getId(), userId))
            throw new ForbiddenException("InitiatorId not equals requestorId");
        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getMyEvents(Long userId, Integer from, Integer size) {
        List<Event> eventList = eventRepository.findMyEvents(userId, from, size);
        return eventList.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    @Override
    public EventFullDto createEvent(NewEventDto newEventDto, Long userId) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2)))
            throw new BadRequestException("Less than 2 hours before EventDate");

        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found."));
        Category category = getCategoryFromDB(newEventDto.getCategory());
        Event eventForSave = EventMapper.toEvent(newEventDto, category, initiator);

        Event saveEvent = eventRepository.save(eventForSave);
        return EventMapper.toEventFullDto(saveEvent);
    }

    @Override
    public EventFullDto patchEvent(UpdateEventRequest updateEventRequest) {
        Event event = getEventFromDB(updateEventRequest.getEventId());

        if (updateEventRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2)))
            throw new BadRequestException("Less than 2 hours before EventDate");

        if (event.getState().equals(EventState.PUBLISHED))
            throw new ForbiddenException("Can't patch PUBLISHED events");
        if (event.getState().equals(EventState.CANCELED))
            event.setState(EventState.PENDING);


        if (updateEventRequest.getTitle() != null)
            event.setTitle(updateEventRequest.getTitle());
        if (updateEventRequest.getAnnotation() != null)
            event.setAnnotation(updateEventRequest.getAnnotation());
        if (updateEventRequest.getDescription() != null)
            event.setDescription(updateEventRequest.getDescription());
        if (updateEventRequest.getCategory() != null)
            event.setCategory(getCategoryFromDB(updateEventRequest.getCategory()));
        if (updateEventRequest.getEventDate() != null)
            event.setEventDate(updateEventRequest.getEventDate());
        if (updateEventRequest.getPaid() != null)
            event.setPaid(updateEventRequest.getPaid());
        if (updateEventRequest.getParticipantLimit() != null)
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());

        Event newEvent = eventRepository.save(event);

        return EventMapper.toEventFullDto(newEvent);
    }

    @Override
    public EventFullDto adminUpdateEvent(AdminUpdateEventRequest updateEventRequest, Long eventId) {
        Event event = getEventFromDB(eventId);

        if (updateEventRequest.getTitle() != null)
            event.setTitle(updateEventRequest.getTitle());
        if (updateEventRequest.getAnnotation() != null)
            event.setAnnotation(updateEventRequest.getAnnotation());
        if (updateEventRequest.getDescription() != null)
            event.setDescription(updateEventRequest.getDescription());
        if (updateEventRequest.getEventDate() != null)
            event.setEventDate(updateEventRequest.getEventDate());
        if (updateEventRequest.getCategory() != null)
            event.setCategory(getCategoryFromDB(updateEventRequest.getCategory()));
        if (updateEventRequest.getLocation() != null) {
            event.setLat(updateEventRequest.getLocation().getLat());
            event.setLon(updateEventRequest.getLocation().getLon());
        }
        if (updateEventRequest.getPaid() != null)
            event.setPaid(updateEventRequest.getPaid());
        if (updateEventRequest.getParticipantLimit() != null)
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        if (updateEventRequest.getRequestModeration() != null)
            event.setRequestModeration(updateEventRequest.getRequestModeration());

        Event updateEvent = eventRepository.save(event);
        return EventMapper.toEventFullDto(updateEvent);
    }

    @Override
    public EventFullDto cancelEvent(Long userId, Long eventId) {
        Event event = getEventFromDB(eventId);

        if (!Objects.equals(event.getInitiator().getId(), userId))
            throw new ForbiddenException("User is not initiator of event");
        if (event.getState() != EventState.PENDING)
            throw new ForbiddenException("Event is not PENDING");

        event.setState(EventState.CANCELED);
        Event newEvent = eventRepository.save(event);
        return EventMapper.toEventFullDto(newEvent);
    }

    @Override
    public EventFullDto publishEvent(Long eventId) {
        Event event = getEventFromDB(eventId);
        if (event.getState() != EventState.PENDING)
            throw new ForbiddenException("Event is not PENDING");
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1)))
            throw new ForbiddenException("Less 1 hour before event.");

        event.setState(EventState.PUBLISHED);
        Event publishEvent = eventRepository.save(event);
        return EventMapper.toEventFullDto(publishEvent);
    }

    @Override
    public EventFullDto rejectEvent(Long eventId) {
        Event event = getEventFromDB(eventId);
        if (event.getState() == EventState.PUBLISHED)
            throw new ForbiddenException("Event is PUBLISHED");

        event.setState(EventState.CANCELED);
        Event rejectEvent = eventRepository.save(event);
        return EventMapper.toEventFullDto(rejectEvent);
    }

    @Override
    @Transactional
    public void changeConfirmRequests(boolean increase, Long eventId) {
        if (increase) {
            eventRepository.changeConfirmedRequests(1, eventId);
        } else {
            eventRepository.changeConfirmedRequests(-1, eventId);
        }
    }

    private Event getEventFromDB(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found."));
    }

    private Category getCategoryFromDB(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + categoryId + " was not found."));
    }

}
