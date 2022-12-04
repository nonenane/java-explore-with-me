package ru.practicum.explorewithme.services;

import ru.practicum.explorewithme.dto.categories.CategoryDto;
import ru.practicum.explorewithme.dto.categories.NewCategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getAll(Integer from, Integer size);

    CategoryDto get(Long catId);

    CategoryDto create(NewCategoryDto categoryDto);

    CategoryDto patch(CategoryDto categoryDto);

    void delete(Long catId);
}
