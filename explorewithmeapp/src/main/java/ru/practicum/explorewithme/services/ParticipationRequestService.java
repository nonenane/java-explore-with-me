package ru.practicum.explorewithme.services;

import ru.practicum.explorewithme.dto.participationrequest.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {
    List<ParticipationRequestDto> getMyEventParticipantRequests(Long userId, Long eventId);

    ParticipationRequestDto moderateParticipantRequest(Long userId, Long eventId, Long reqId, Boolean confirm);

    List<ParticipationRequestDto> getMyRequests(Long userId);

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
