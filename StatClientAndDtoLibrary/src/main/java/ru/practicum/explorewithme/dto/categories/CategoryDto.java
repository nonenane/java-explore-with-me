package ru.practicum.explorewithme.dto.categories;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CategoryDto {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
}
