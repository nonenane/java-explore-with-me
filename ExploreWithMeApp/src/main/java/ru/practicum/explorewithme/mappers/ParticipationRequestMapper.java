package ru.practicum.explorewithme.mappers;

import ru.practicum.explorewithme.dto.participationrequest.ParticipationRequestDto;
import ru.practicum.explorewithme.models.ParticipationRequest;

public class ParticipationRequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest request) {
        if (request == null)
            return null;

        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }
}
