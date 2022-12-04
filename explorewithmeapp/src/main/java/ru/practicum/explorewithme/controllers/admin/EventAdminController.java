package ru.practicum.explorewithme.controllers.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.EventState;
import ru.practicum.explorewithme.dto.event.AdminUpdateEventRequest;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.services.EventService;
import ru.practicum.explorewithme.storages.event.EventSearchParamsModel;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/events")
public class EventAdminController {

    private final EventService eventService;

    public EventAdminController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Поиск событий
     */
    @GetMapping()
    public List<EventFullDto> getEventsAdmin(@RequestParam(required = false) Set<Long> users,
                                             @RequestParam(required = false) Set<EventState> states,
                                             @RequestParam(required = false) Set<Long> categories,
                                             @RequestParam(required = false) LocalDateTime rangeStart,
                                             @RequestParam(required = false) LocalDateTime rangeEnd,
                                             @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        EventSearchParamsModel paramModel = EventSearchParamsModel.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .from(from)
                .size(size)
                .build();
        List<EventFullDto> dtoList = eventService.getEventsAdmin(paramModel);
        log.info("Выполнен запрос Поиска события с параметрами Users {}, States {}, Categories {}, RangeStart {},RangeEnd {}," +
                "From {},Size {}", users, states, categories, rangeStart, rangeEnd, from, size);
        return dtoList;
    }

    /**
     * Редактирование событий
     */
    @PutMapping("/{eventId}")
    public EventFullDto updateEventAdmin(@RequestBody AdminUpdateEventRequest adminUpdateEventRequest,
                                         @PathVariable Long eventId) {

        EventFullDto updateEvent = eventService.adminUpdateEvent(adminUpdateEventRequest, eventId);
        log.info("Выполнен запрос Обновления события {}", adminUpdateEventRequest);
        return updateEvent;
    }

    /**
     * Публикация события
     */
    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable Long eventId) {
        EventFullDto event = eventService.publishEvent(eventId);
        log.info("Выполнен запрос Публикации события ID {}", eventId);
        return event;
    }

    /**
     * Отклонение события
     */
    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable Long eventId) {
        EventFullDto event = eventService.rejectEvent(eventId);
        log.info("Выполнен запрос Отклонения события ID {}", eventId);
        return event;
    }

}
