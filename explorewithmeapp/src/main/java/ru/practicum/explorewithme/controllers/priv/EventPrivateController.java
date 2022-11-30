package ru.practicum.explorewithme.controllers.priv;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.dto.event.UpdateEventRequest;
import ru.practicum.explorewithme.dto.participationrequest.ParticipationRequestDto;
import ru.practicum.explorewithme.services.EventService;
import ru.practicum.explorewithme.services.ParticipationRequestService;
import ru.practicum.explorewithme.services.authentication.AuthenticationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users/{userId}")
public class EventPrivateController {

    private final AuthenticationService authenticationService;
    private final EventService eventService;
    private final ParticipationRequestService requestService;

    public EventPrivateController(AuthenticationService authenticationService,
                                  EventService eventService,
                                  ParticipationRequestService requestService) {
        this.authenticationService = authenticationService;
        this.eventService = eventService;
        this.requestService = requestService;
    }

    /**
     * Добавление нового события
     */
    @PostMapping("/events")
    public EventFullDto createEvent(@Valid @RequestBody NewEventDto newEventDto,
                                    @PathVariable Long userId) {
        authenticationService.throwIfUserNotFound(userId);
        EventFullDto eventFullDto = eventService.create(newEventDto, userId);
        log.info("Выполнен запрос Создание события {}", newEventDto);
        return eventFullDto;
    }

    /**
     * Изменение события, добавленного текущим пользователем
     */
    @PatchMapping("/events")
    public EventFullDto patchEvent(@Valid @RequestBody UpdateEventRequest updateEventRequest,
                                   @PathVariable Long userId) {
        authenticationService.throwIfUserNotFound(userId);
        EventFullDto updateEvent = eventService.patch(updateEventRequest);
        log.info("Выполнен запрос Обновления события по ID {}, {}", userId, updateEventRequest);
        return updateEvent;
    }

    /**
     * Отмена события,добавленного текущим пользователем
     */
    @PatchMapping("/events/{eventId}")
    public EventFullDto cancelEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        authenticationService.throwIfUserNotFound(userId);
        EventFullDto updateEvent = eventService.cancelEvent(userId, eventId);
        log.info("Выполнен запрос Отмены события ID {} пользователем ID {}", eventId, userId);
        return updateEvent;
    }

    /**
     * Получение событий, добавленных текущим пользователем
     */
    @GetMapping("/events")
    public List<EventShortDto> getMyEvents(@PathVariable Long userId,
                                           @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        authenticationService.throwIfUserNotFound(userId);
        List<EventShortDto> dtoList = eventService.getMyEvents(userId, from, size);
        log.info("Выполнен запрос Получение событий, добавленных текущим пользователем ID {}", userId);
        return dtoList;
    }

    /**
     * Получение полной информации о событии, добавленном текущм пользователем
     */
    @GetMapping("/events/{eventId}")
    public EventFullDto getMyEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        authenticationService.throwIfUserNotFound(userId);
        EventFullDto event = eventService.getMyEvent(userId, eventId);
        log.info("Выполнен запрос Получение полной информации о событии ID {}, добавленном текущм пользователем ID {}",
                eventId, userId);
        return event;
    }

    /**
     * Получение информации о запросах на участие в событии, добавленном текущм пользователем
     */
    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getMyEventParticipantRequests(@PathVariable Long userId,
                                                                       @PathVariable Long eventId) {
        authenticationService.throwIfUserNotFound(userId);
        List<ParticipationRequestDto> requestDtoList = requestService.getMyEventParticipantRequests(userId, eventId);
        log.info("Выполнен запрос Получение информации о запросах на участие в событии ID {}, " +
                "добавленном текущм пользователем ID", eventId, userId);
        return requestDtoList;
    }

    /**
     * Подтверждение чужой заявки на участие в событии текущего пользователя
     */
    @PatchMapping("/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmParticipantRequest(@PathVariable Long userId,
                                                             @PathVariable Long eventId,
                                                             @PathVariable Long reqId) {
        authenticationService.throwIfUserNotFound(userId);
        ParticipationRequestDto requestDto = requestService.moderateParticipantRequest(userId,
                eventId,
                reqId,
                true);
        log.info("Выполнен запрос Подтверждение чужой заявки ID {} на участие в событии ID {}" +
                "текущего пользователя ID {}", reqId, eventId, userId);
        return requestDto;
    }

    /**
     * Отклонение чужой заявки на участие в событии текущего пользователя
     */
    @PatchMapping("/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectParticipantRequest(@PathVariable Long userId,
                                                            @PathVariable Long eventId,
                                                            @PathVariable Long reqId) {
        authenticationService.throwIfUserNotFound(userId);
        ParticipationRequestDto requestDto = requestService.moderateParticipantRequest(userId,
                eventId,
                reqId,
                false);
        log.info("Выполнен запрос Отклонение чужой заявки ID {} на участие в событии ID {}" +
                "текущего пользователя ID {}", reqId, eventId, userId);
        return requestDto;
    }
}