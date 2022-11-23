package ru.practicum.explorewithme.storages;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.models.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query(value = "select * " +
            "from compilations " +
            "limit ?2 offset ?1", nativeQuery = true)
    List<Compilation> searchCompilationsPage(Integer from, Integer size);

    /**
     * int pinned = "1" или "0" (true/false)
     */
    @Query(value = "select * " +
            "from compilations as c " +
            "where c.pinned = CAST(?1 as boolean) " +
            "limit ?3 offset ?2", nativeQuery = true)
    List<Compilation> searchCompilationsPageWithPinned(Boolean pinned, Integer from, Integer size);

    @Modifying
    @Query(value = "delete from events_compilation as ec " +
            "where ec.compilation_id=?1 and ec.event_id=?2", nativeQuery = true)
    void deleteEventFromCompilation(Long compId, Long eventId);

    @Modifying
    @Query(value = "insert into events_compilation (compilation_id, event_id)" +
            "values (?1, ?2)", nativeQuery = true)
    void addEventToCompilation(Long compId, Long eventId);
}
