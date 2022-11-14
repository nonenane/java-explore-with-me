package ru.practicum.explorewithme.models;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String name;
    @Column(unique = true)
    private String email;

    //id пользователей, на которых подписан данный пользователь
    @ElementCollection
    @CollectionTable(name = "subscriptions", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "subscriptions_id")
    private Set<Long> subscriptions = new HashSet<>();
}
