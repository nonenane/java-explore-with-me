package ru.practicum.explorewithme.mappers;

import ru.practicum.explorewithme.EventState;
import ru.practicum.explorewithme.Location;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.models.Category;
import ru.practicum.explorewithme.models.Event;
import ru.practicum.explorewithme.models.User;

import java.time.LocalDateTime;
import java.util.HashSet;

public class EventMapper {

    public static EventShortDto toEventShortDto(Event event) {
        if (event == null)
            return null;

        return EventShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(CategoriesMapper.toCategoryDto(event.getCategory()))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .eventDate(event.getEventDate())
                .paid(event.getPaid())
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getViews())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        if (event == null)
            return null;

        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .category(CategoriesMapper.toCategoryDto(event.getCategory()))
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(new Location(event.getLat(), event.getLon()))
                .eventDate(event.getEventDate())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .confirmedRequests(event.getConfirmedRequests())
                .state(event.getState())
                .views(event.getViews())
                .build();
    }

    public static Event toEvent(NewEventDto newEventDto, Category category, User initiator) {
        if (newEventDto == null)
            return null;

        return Event.builder()
                .title(newEventDto.getTitle())
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .category(category)
                .confirmedRequests(0)
                .createdOn(LocalDateTime.now())
                .eventDate(newEventDto.getEventDate())
                .initiator(initiator)
                .lat(newEventDto.getLocation().getLat())
                .lon(newEventDto.getLocation().getLon())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit() == null ? 0 : newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration() == null || newEventDto.getRequestModeration())
                .state(EventState.PENDING)
                .views(0)
                .compilations(new HashSet<>())
                .build();
    }
}
