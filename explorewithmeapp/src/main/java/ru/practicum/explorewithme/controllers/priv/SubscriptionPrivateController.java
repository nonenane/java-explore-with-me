package ru.practicum.explorewithme.controllers.priv;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.EventsSortType;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.services.SubscriptionService;
import ru.practicum.explorewithme.services.authentication.AuthenticationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users/{userId}")
public class SubscriptionPrivateController {

    private final AuthenticationService authenticationService;
    private final SubscriptionService subscriptionService;

    public SubscriptionPrivateController(AuthenticationService authenticationService,
                                         SubscriptionService subscriptionService) {
        this.authenticationService = authenticationService;
        this.subscriptionService = subscriptionService;
    }

    /**
     * Подписаться на пользователя (фича)
     */
    @PostMapping("/subscription/{subsId}")
    public void createSubscription(@PathVariable Long userId, @PathVariable Long subsId) {
        authenticationService.throwIfUserNotFound(userId);
        subscriptionService.create(userId, subsId);
        log.info("Выполнен запрос Подписаться на пользователя ID {} пользователем ID {}",
                subsId, userId);
    }

    /**
     * Отменить подписку (фича)
     */
    @DeleteMapping("/subscription/{subsId}")
    public void deleteSubscription(@PathVariable Long userId, @PathVariable Long subsId) {
        authenticationService.throwIfUserNotFound(userId);
        subscriptionService.delete(userId, subsId);
        log.info("Выполнен запрос Отменить подписку на пользователя ID {} пользователем ID {}",
                subsId, userId);
    }


    /**
     * Посмотреться грядущие события пользователей, на которых подписан (фича)
     */
    @GetMapping("/subscription")
    public List<EventShortDto> getSubscriptionEvents(@PathVariable Long userId,
                                                     @RequestParam(required = false) EventsSortType sort,
                                                     @RequestParam(required = false, defaultValue = "0")
                                                     @PositiveOrZero Integer from,
                                                     @RequestParam(required = false, defaultValue = "10")
                                                     @Positive Integer size) {
        authenticationService.throwIfUserNotFound(userId);
        List<EventShortDto> event = subscriptionService.get(userId, sort, from, size);
        log.info("Выполнен запрос Посмотреться грядущие события пользователей, на которых подписан ID {}",
                userId);
        return event;
    }
}
