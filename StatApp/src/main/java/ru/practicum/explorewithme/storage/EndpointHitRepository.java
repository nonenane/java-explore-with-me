package ru.practicum.explorewithme.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {

    @Query(value = "select count(*) " +
            "from endpoint_hits as eh " +
            "where eh.uri=?1 and eh.timestamp between ?2 and ?3", nativeQuery = true)
    Optional<Integer> findUriHits(String uri, LocalDateTime start, LocalDateTime end);

    @Query(value = "select count(DISTINCT eh.ip) " +
            "from endpoint_hits as eh " +
            "where eh.uri=?1 and eh.timestamp between ?2 and ?3", nativeQuery = true)
    Optional<Integer> findUniqueIpUriHits(String uri, LocalDateTime start, LocalDateTime end);
}
