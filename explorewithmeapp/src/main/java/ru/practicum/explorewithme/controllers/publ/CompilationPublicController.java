package ru.practicum.explorewithme.controllers.publ;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.services.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
public class CompilationPublicController {

    private final CompilationService compilationService;

    public CompilationPublicController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    /**
     * Получение подборок событий
     */
    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(required = false, defaultValue = "0")
                                                @PositiveOrZero Integer from,
                                                @RequestParam(required = false, defaultValue = "10")
                                                @Positive Integer size) {

        List<CompilationDto> compilationsDto = compilationService.getAll(pinned, from, size);
        log.info("Выполнен запрос getCompilations");
        return compilationsDto;
    }

    /**
     * Получение подборки событий по id
     */
    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {

        CompilationDto compilationDto = compilationService.get(compId);
        log.info("Выполнен запрос getCompilation");
        return compilationDto;
    }
}
