package ru.practicum.explorewithme.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.statistic.ViewStats;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.service.EndpointHitService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Validated
@RestController
public class StatsController {

    private final EndpointHitService endpointHitService;

    public StatsController(EndpointHitService endpointHitService) {
        this.endpointHitService = endpointHitService;
    }

    /*
    Сохранение информации о том, что к эндпоинту был запрос
     */
    @PostMapping("/hit")
    public void createEndpointHit(@RequestBody EndpointHit endpointHit) {
        endpointHitService.createEndpointHit(endpointHit);
        log.info("Выполнен запрос createEndpointHit");
    }

    /*
    Получение статистики по посещениям
     */
    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam LocalDateTime start,
                                    @RequestParam LocalDateTime end,
                                    @RequestParam Set<String> uris,
                                    @RequestParam(required = false, defaultValue = "false") Boolean unique) {

        List<ViewStats> statsList = endpointHitService.getStats(start, end, uris, unique);
        log.info("Выполнен запрос getStats");
        return statsList;
    }
}
