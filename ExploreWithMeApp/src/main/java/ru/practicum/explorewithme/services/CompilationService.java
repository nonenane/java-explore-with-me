package ru.practicum.explorewithme.services;

import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getCompilations(Boolean pinned,
                                         Integer from,
                                         Integer size);

    CompilationDto getCompilation(Long compId);

    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compId);

    void deleteEventFromCompilation(Long compId, Long eventId);

    void addEventToCompilation(Long compId, Long eventId);

    void unpinCompilation(@PathVariable Long compId);

    void pinCompilation(@PathVariable Long compId);
}
