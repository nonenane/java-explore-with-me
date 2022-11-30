package ru.practicum.explorewithme.controllers.priv;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.participationrequest.ParticipationRequestDto;
import ru.practicum.explorewithme.services.ParticipationRequestService;
import ru.practicum.explorewithme.services.authentication.AuthenticationService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users/{userId}")
public class ParticipationRequestPrivateController {

    private final AuthenticationService authenticationService;
    private final ParticipationRequestService requestService;

    public ParticipationRequestPrivateController(AuthenticationService authenticationService,
                                                 ParticipationRequestService requestService) {
        this.authenticationService = authenticationService;
        this.requestService = requestService;
    }

    /**
     * Получение информации о заявках данного пользователя на участие в чужих событиях
     */
    @GetMapping("/requests")
    public List<ParticipationRequestDto> getMyRequests(@PathVariable Long userId) {
        authenticationService.throwIfUserNotFound(userId);
        List<ParticipationRequestDto> requestDtoList = requestService.getMyRequests(userId);
        log.info("Выполнен запрос Получение информации о заявках данного пользователя ID {} на участие " +
                "в чужих событиях", userId);
        return requestDtoList;
    }

    /**
     * Добавление запроса на участие в событии
     */
    @PostMapping("/requests")
    public ParticipationRequestDto createRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        authenticationService.throwIfUserNotFound(userId);
        ParticipationRequestDto savedRequest = requestService.createRequest(userId, eventId);
        log.info("Выполнен запрос Добавление запроса на участие в событии ID {} пользователем ID {}",
                eventId, userId);
        return savedRequest;
    }

    /**
     * Отмена своего запроса на участие
     */
    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        authenticationService.throwIfUserNotFound(userId);
        ParticipationRequestDto requestDto = requestService.cancelRequest(userId, requestId);
        log.info("Выполнен запрос Отмена своего запроса на участие в событие ID {} пользователь ID {}",
                requestId, userId);
        return requestDto;
    }
}
