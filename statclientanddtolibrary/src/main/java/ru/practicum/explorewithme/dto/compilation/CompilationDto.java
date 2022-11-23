package ru.practicum.explorewithme.dto.compilation;

import lombok.*;
import ru.practicum.explorewithme.dto.event.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CompilationDto {
    @NotNull
    private Long id;
    @NotNull
    private Boolean pinned;
    @NotBlank
    String title;
    private List<EventShortDto> events;
}
