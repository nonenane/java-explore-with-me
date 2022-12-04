package ru.practicum.explorewithme.services;

import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getAll(Boolean pinned,
                                Integer from,
                                Integer size);

    CompilationDto get(Long compId);

    CompilationDto create(NewCompilationDto newCompilationDto);

    void delete(Long compId);

    void deleteEventFromCompilation(Long compId, Long eventId);

    void addEventToCompilation(Long compId, Long eventId);

    void unpinCompilation(@PathVariable Long compId);

    void pinCompilation(@PathVariable Long compId);
}
