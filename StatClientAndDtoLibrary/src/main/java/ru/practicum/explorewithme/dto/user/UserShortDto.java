package ru.practicum.explorewithme.dto.user;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Пользователь (краткая информация)
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserShortDto {
    @NotNull
    public Long id;
    @NotBlank
    public String name;
}
