package ru.practicum.explorewithme.storages.event;

import lombok.*;
import ru.practicum.explorewithme.EventState;
import ru.practicum.explorewithme.EventsSortType;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EventSearchParamsModel {
    private Set<Long> users;
    private Set<EventState> states;
    private Set<Long> categories;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private String text;
    private Boolean paid;
    private Boolean onlyAvailable;
    private EventsSortType sort;
    private Integer from;
    private Integer size;
}
