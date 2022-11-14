package ru.practicum.explorewithme.storages;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.models.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "select * " +
            "from categories " +
            "limit ?2 offset ?1", nativeQuery = true)
    List<Category> searchCategoriesPage(Integer from, Integer size);
}
