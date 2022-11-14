package ru.practicum.explorewithme.storages;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.ParticipationStatus;
import ru.practicum.explorewithme.models.ParticipationRequest;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findAllByEvent_Id(Long eventId);

    List<ParticipationRequest> findAllByRequester_Id(Long requesterId);

    Optional<ParticipationRequest> findByEvent_IdAndRequester_Id(Long eventId, Long requesterId);

    /*
    Например: Отклонить(REJECTED) все неподтвержденные(PENDING) заявки
     */
    @Modifying
    @Query(value = "update participation_requests as pr set pr.status = ?1 " +
            "where pr.event_id = ?2 and pr.status = ?3", nativeQuery = true)
    int changeStatusForEventRequests(ParticipationStatus newStatus, Long eventId, ParticipationStatus oldStatus);
}
