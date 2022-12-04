package ru.practicum.explorewithme.models;

import lombok.*;
import ru.practicum.explorewithme.EventState;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;
    @Column(name = "title", length = 120, nullable = false)
    private String title;
    @Column(name = "annotation", length = 2000, nullable = false)
    private String annotation;
    @Column(name = "description", length = 7000)
    private String description;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "initiator_id")
    private User initiator;
    //координаты, широта
    @Column(name = "lat")
    private Double lat;
    //координаты, долгота
    @Column(name = "lon")
    private Double lon;
    @Column(name = "paid", nullable = false)
    private Boolean paid;
    @Column(name = "participant_limit", nullable = false)
    private Integer participantLimit;
    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 16)
    private EventState state;
    @Column(name = "views")
    private Integer views;

    @ManyToMany(mappedBy = "events", cascade = {CascadeType.ALL})
    private Set<Compilation> compilations = new HashSet<>();
}
