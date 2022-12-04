package ru.practicum.explorewithme.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.exceptions.ConflictException;
import ru.practicum.explorewithme.exceptions.ForbiddenException;
import ru.practicum.explorewithme.exceptions.NotFoundException;
import ru.practicum.explorewithme.mappers.CompilationMapper;
import ru.practicum.explorewithme.models.Compilation;
import ru.practicum.explorewithme.models.Event;
import ru.practicum.explorewithme.services.CompilationService;
import ru.practicum.explorewithme.storages.CompilationRepository;
import ru.practicum.explorewithme.storages.event.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public CompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {

        List<Compilation> compilationsList;

        if (pinned == null) {
            compilationsList = compilationRepository.searchCompilationsPage(from, size);
        } else {
            compilationsList = compilationRepository.searchCompilationsPageWithPinned(pinned, from, size);
        }

        return compilationsList.stream().map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
    }

    @Override
    public CompilationDto get(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found."));
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        Compilation compilation = new Compilation(null,
                newCompilationDto.getPinned(),
                newCompilationDto.getTitle(),
                newCompilationDto.getEvents()
                        .stream()
                        .map(eventId -> eventRepository.findById(eventId)
                                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found.")))
                        .collect(Collectors.toSet()));
        Compilation newCompilation = compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDto(newCompilation);
    }

    @Override
    public void delete(Long compId) {
        compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found."));
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public void deleteEventFromCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found."));
        compilation.getEvents().removeIf(event -> event.getId().equals(eventId));
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void addEventToCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found."));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found."));
        if (!compilation.getEvents().contains(event)) {
            compilation.getEvents().add(event);
            compilationRepository.save(compilation);
        } else {
            throw new ConflictException("Compilation already contain this event");
        }
    }

    @Override
    public void unpinCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found."));
        if (!compilation.getPinned())
            throw new ForbiddenException("Compilation is not pinned");
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public void pinCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found."));
        if (compilation.getPinned())
            throw new ForbiddenException("Compilation is already pinned");
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

}
