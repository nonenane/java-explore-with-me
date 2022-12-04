package ru.practicum.explorewithme.storages.event;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.EventState;
import ru.practicum.explorewithme.EventsSortType;
import ru.practicum.explorewithme.models.Event;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class EventCriteriaRepositoryImpl implements EventCriteriaRepository {

    private final EntityManager entityManager;

    public EventCriteriaRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Event> publicSearchByParameters(EventSearchParamsModel paramModel) {

        String text = paramModel.getText();
        Set<Long> categories = paramModel.getCategories();
        Boolean paid = paramModel.getPaid();
        LocalDateTime rangeStart = paramModel.getRangeStart();
        LocalDateTime rangeEnd = paramModel.getRangeEnd();
        Boolean onlyAvailable = paramModel.getOnlyAvailable();
        EventsSortType sort = paramModel.getSort();
        Integer from = paramModel.getFrom();
        Integer size = paramModel.getSize();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> cr = cb.createQuery(Event.class);
        Root<Event> root = cr.from(Event.class);

        List<Predicate> predicateList = new ArrayList<>();
        predicateList.add(cb.equal(root.get("state"), EventState.PUBLISHED));

        if (text != null)
            predicateList.add(cb.or(cb.like(cb.lower(root.get("annotation")), "%" + text.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("description")), "%" + text.toLowerCase() + "%")));

        if (rangeStart != null && rangeEnd != null) {
            predicateList.add(cb.and(cb.greaterThan(root.get("eventDate"), rangeStart),
                    cb.lessThan(root.get("eventDate"), rangeEnd)));
        } else {
            predicateList.add(cb.greaterThan(root.get("eventDate"), LocalDateTime.now()));
        }

        if (categories != null && !categories.isEmpty())
            predicateList.add(root.get("category").get("id").in(categories));

        if (paid != null)
            predicateList.add(cb.equal(root.get("paid"), paid));

        if (onlyAvailable)
            predicateList.add(cb.and(cb.between(root.get("participantLimit"), -1, 1),
                    cb.lessThan(root.get("confirmedRequests"), root.get("participantLimit"))));

        sortEventsCheck(sort, predicateList, cb, cr, root);

        return entityManager.createQuery(cr).setFirstResult(from).setMaxResults(size).getResultList();
    }

    @Override
    public List<Event> adminSearchByParameters(EventSearchParamsModel paramModel) {

        Set<Long> users = paramModel.getUsers();
        Set<EventState> states = paramModel.getStates();
        Set<Long> categories = paramModel.getCategories();
        LocalDateTime rangeStart = paramModel.getRangeStart();
        LocalDateTime rangeEnd = paramModel.getRangeEnd();
        Integer from = paramModel.getFrom();
        Integer size = paramModel.getSize();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> cr = cb.createQuery(Event.class);
        Root<Event> root = cr.from(Event.class);

        List<Predicate> predicateList = new ArrayList<>();

        if (users != null && !users.isEmpty())
            predicateList.add(root.get("initiator").get("id").in(users));

        if (states != null && !states.isEmpty())
            predicateList.add(root.get("state").in(states));

        if (categories != null && !categories.isEmpty())
            predicateList.add(root.get("category").get("id").in(categories));

        if (rangeStart != null && rangeEnd != null) {
            predicateList.add(cb.and(cb.greaterThan(root.get("eventDate"), rangeStart),
                    cb.lessThan(root.get("eventDate"), rangeEnd)));
        } else {
            predicateList.add(cb.greaterThan(root.get("eventDate"), LocalDateTime.now()));
        }


        cr.select(root).where(predicateList.toArray(new Predicate[0]));

        return entityManager.createQuery(cr).setFirstResult(from).setMaxResults(size).getResultList();
    }

    @Override
    public List<Event> findSubscriptionsEvents(List<Long> subsId, EventsSortType sort, Integer from, Integer size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> cr = cb.createQuery(Event.class);
        Root<Event> root = cr.from(Event.class);

        List<Predicate> predicateList = new ArrayList<>();

        predicateList.add(cb.greaterThan(root.get("eventDate"), LocalDateTime.now()));

        if (subsId != null && !subsId.isEmpty())
            predicateList.add(root.get("initiator").get("id").in(subsId));

        sortEventsCheck(sort, predicateList, cb, cr, root);

        return entityManager.createQuery(cr).setFirstResult(from).setMaxResults(size).getResultList();
    }

    private void sortEventsCheck(EventsSortType sort,
                                 List<Predicate> predicateList,
                                 CriteriaBuilder cb,
                                 CriteriaQuery<Event> cr,
                                 Root<Event> root) {
        if (sort != null) {
            switch (sort) {
                case VIEWS:
                    cr.select(root).where(predicateList.toArray(new Predicate[0]))
                            .orderBy(cb.desc(root.get("views")));
                    break;
                case EVENT_DATE:
                    cr.select(root).where(predicateList.toArray(new Predicate[0]))
                            .orderBy(cb.desc(root.get("eventDate")));
                    break;
                default:
            }
        } else {
            cr.select(root).where(predicateList.toArray(new Predicate[0]));
        }
    }
}
