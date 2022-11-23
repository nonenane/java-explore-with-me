package ru.practicum.explorewithme.controllers.publ;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.categories.CategoryDto;
import ru.practicum.explorewithme.services.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
public class CategoryPublicController {

    private final CategoryService categoryService;

    public CategoryPublicController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Получение категорий
     */
    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(required = false, defaultValue = "0")
                                           @PositiveOrZero Integer from,
                                           @RequestParam(required = false, defaultValue = "10")
                                           @Positive Integer size) {

        List<CategoryDto> dtoList = categoryService.getAll(from, size);
        log.info("Выполнен запрос getCategories");
        return dtoList;
    }

    /**
     * Получение информации о категории по id
     */
    @GetMapping("/categories/{catId}")
    public CategoryDto getCategory(@PathVariable Long catId) {

        CategoryDto categoryDto = categoryService.get(catId);
        log.info("Выполнен запрос getCategory");
        return categoryDto;
    }
}
