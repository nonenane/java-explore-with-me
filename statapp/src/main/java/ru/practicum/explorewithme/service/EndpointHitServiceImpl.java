package ru.practicum.explorewithme.service;

import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.statistic.ViewStats;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.storage.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class EndpointHitServiceImpl implements EndpointHitService {

    private final EndpointHitRepository endpointHitRepository;

    public EndpointHitServiceImpl(EndpointHitRepository endpointHitRepository) {
        this.endpointHitRepository = endpointHitRepository;
    }

    @Override
    public void createEndpointHit(EndpointHit endpointHit) {
        endpointHitRepository.save(endpointHit);
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, Set<String> uris, Boolean unique) {
        List<ViewStats> statsList = new ArrayList<>();
        if (uris.isEmpty())
            return statsList;

        for (String uri : uris) {
            Integer hits;
            if (unique) {
                hits = endpointHitRepository.findUniqueIpUriHits(uri, start, end)
                        .orElseThrow(() -> new RuntimeException("DB extraction error"));
            } else {
                hits = endpointHitRepository.findUriHits(uri, start, end)
                        .orElseThrow(() -> new RuntimeException("DB extraction error"));
            }
            ViewStats viewStats = new ViewStats("ewm-main-service", uri, hits);
            statsList.add(viewStats);
        }

        return statsList;
    }
}
