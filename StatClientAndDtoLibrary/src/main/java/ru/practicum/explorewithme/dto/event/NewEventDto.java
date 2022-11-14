package ru.practicum.explorewithme.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.explorewithme.Location;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Данные для добавления нового события.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NewEventDto {
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    @Size(min = 20, max = 7000)
    private String description;
    @NotNull
    private Long category;
    @NotNull
    @Future
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    @NotNull
    private Boolean paid;
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;
}
