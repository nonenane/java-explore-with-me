package ru.practicum.explorewithme.services.statclient;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.explorewithme.dto.statistic.EndpointHitDto;
import ru.practicum.explorewithme.dto.statistic.ViewStats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Lazy
public class StatClient {

    private final WebClient client;
    private final Environment env;

    public StatClient(Environment env) {
        this.env = env;
        this.client = WebClient.create();
    }

    public ResponseEntity<String> createEndpointHit(EndpointHitDto endpointHitDto) {
        String serverUri = env.getProperty("stat-server.url");
        return client.post()
                .uri(serverUri + "/hit")
                .body(Mono.just(endpointHitDto), EndpointHitDto.class)
                .retrieve()
                .toEntity(String.class)
                .block();
    }

    public List<ViewStats> getStats(LocalDateTime start,
                                    LocalDateTime end,
                                    Set<String> uris,
                                    Boolean unique) {
        String serverUri = env.getProperty("stat-server.url");
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(serverUri + "/stats/")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("unique", unique)
                        .queryParam("uris", String.join(",", uris))
                        .build())
                .retrieve()
                .bodyToFlux(ViewStats.class)
                .collect(Collectors.toList())
                .block();
    }
}
