package ru.practicum.explorewithme.controllers.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.categories.CategoryDto;
import ru.practicum.explorewithme.dto.categories.NewCategoryDto;
import ru.practicum.explorewithme.services.CategoryService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/categories")
public class CategoryAdminController {

    private final CategoryService categoryService;

    public CategoryAdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping()
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto categoryDto) {

        CategoryDto newCategoryDto = categoryService.create(categoryDto);
        log.info("Выполнен запрос createCategory");
        return newCategoryDto;
    }

    @PatchMapping()
    public CategoryDto patchCategory(@Valid @RequestBody CategoryDto categoryDto) {

        CategoryDto newCategoryDto = categoryService.patch(categoryDto);
        log.info("Выполнен запрос patchCategory");
        return newCategoryDto;
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable Long catId) {

        categoryService.delete(catId);
        log.info("Выполнен запрос patchCategory");
    }
}
