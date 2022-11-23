package ru.practicum.explorewithme.storages.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.models.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, EventCriteriaRepository {

    @Query(value = "select * " +
            "from events as e " +
            "where e.initiator_id = ?1 " +
            "limit ?3 offset ?2", nativeQuery = true)
    List<Event> findMyEvents(Long userId, Integer from, Integer size);

    /*
    Изменяет количество подтвержденных заявок на участие
     */
    @Modifying
    @Query(value = "update events set confirmed_requests = confirmed_requests + ?1 " +
            "where event_id = ?2 ", nativeQuery = true)
    int changeConfirmedRequests(int num, Long eventId);

    List<Event> findAllByCategory_id(Long catId);
}
