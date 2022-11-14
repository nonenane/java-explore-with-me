package ru.practicum.explorewithme;

import lombok.*;

/**
 * Класс, в котором хранятся широта и долгота места проведения события.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Location {
    Double lat;
    Double lon;
}
