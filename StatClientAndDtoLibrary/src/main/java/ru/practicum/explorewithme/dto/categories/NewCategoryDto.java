package ru.practicum.explorewithme.dto.categories;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * Данные для добавления новой категории
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NewCategoryDto {
    @NotBlank
    private String name;
}
