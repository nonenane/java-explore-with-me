package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.statistic.ViewStats;
import ru.practicum.explorewithme.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EndpointHitService {

    void createEndpointHit(EndpointHit endpointHit);

    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, Set<String> uris, Boolean unique);
}
