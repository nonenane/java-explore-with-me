package ru.practicum.explorewithme.services.impl;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.categories.CategoryDto;
import ru.practicum.explorewithme.dto.categories.NewCategoryDto;
import ru.practicum.explorewithme.exceptions.ConflictException;
import ru.practicum.explorewithme.exceptions.ForbiddenException;
import ru.practicum.explorewithme.exceptions.NotFoundException;
import ru.practicum.explorewithme.mappers.CategoriesMapper;
import ru.practicum.explorewithme.models.Category;
import ru.practicum.explorewithme.models.Event;
import ru.practicum.explorewithme.services.CategoryService;
import ru.practicum.explorewithme.storages.CategoryRepository;
import ru.practicum.explorewithme.storages.event.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.explorewithme.mappers.CategoriesMapper.toCategoryDto;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        return categoryRepository.searchCategoriesPage(from, size)
                .stream()
                .map(CategoriesMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto get(Long catId) {
        Category category = getCategoryFromDB(catId);
        return toCategoryDto(category);
    }

    @Override
    public CategoryDto create(NewCategoryDto categoryDto) {
        try {
            Category category = new Category(null, categoryDto.getName());
            Category newCategory = categoryRepository.save(category);
            return toCategoryDto(newCategory);
        } catch (ConstraintViolationException | DataIntegrityViolationException exp) {
            throw new ConflictException(String.format("Ошибка добавления категории %s", categoryDto));
        }
    }

    @Override
    public CategoryDto patch(CategoryDto categoryDto) {
        try {
            Category oldCategory = getCategoryFromDB(categoryDto.getId());
            oldCategory.setName(categoryDto.getName());
            Category patchCategory = categoryRepository.save(oldCategory);
            return toCategoryDto(patchCategory);
        } catch (ConstraintViolationException | DataIntegrityViolationException exp) {
            throw new ConflictException(String.format("Ошибка обновления категории %s", categoryDto));
        }
    }

    @Override
    public void delete(Long catId) {
        Category category = getCategoryFromDB(catId);
        List<Event> eventList = eventRepository.findAllByCategory_id(catId);
        if (!eventList.isEmpty())
            throw new ForbiddenException(String.format("Find event with this category %s not found", category.getName()));
        categoryRepository.deleteById(catId);
    }

    private Category getCategoryFromDB(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + categoryId + " was not found."));
    }
}
