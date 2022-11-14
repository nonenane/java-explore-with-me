package ru.practicum.explorewithme.mappers;

import ru.practicum.explorewithme.dto.categories.CategoryDto;
import ru.practicum.explorewithme.models.Category;

public class CategoriesMapper {
    public static CategoryDto toCategoryDto(Category category) {
        if (category == null)
            return null;

        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
