package ru.practicum.explorewithme.storages.event;

import ru.practicum.explorewithme.EventsSortType;
import ru.practicum.explorewithme.models.Event;

import java.util.List;

public interface EventCriteriaRepository {

    List<Event> publicSearchByParameters(EventSearchParamsModel paramModel);

    List<Event> adminSearchByParameters(EventSearchParamsModel paramModel);

    List<Event> findSubscriptionsEvents(List<Long> subsId,
                                        EventsSortType sort,
                                        Integer from,
                                        Integer size);
}
