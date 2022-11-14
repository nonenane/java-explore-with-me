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
    private String title;
    private String annotation;
    private String description;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;
    private LocalDateTime eventDate;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "initiator_id")
    private User initiator;
    //координаты, широта
    private Double lat;
    //координаты, долгота
    private Double lon;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private EventState state;
    private Integer views;

    @ManyToMany(mappedBy = "events", cascade = {CascadeType.ALL})
    private Set<Compilation> compilations = new HashSet<>();
}
