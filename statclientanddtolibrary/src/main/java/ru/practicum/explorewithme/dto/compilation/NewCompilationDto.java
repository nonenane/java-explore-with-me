package ru.practicum.explorewithme.dto.compilation;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

/**
 * Данные для добавления новой подборки событий
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NewCompilationDto {

    @NotBlank
    private String title;
    private Boolean pinned = false;
    private Set<Long> events = new HashSet<>();
}
