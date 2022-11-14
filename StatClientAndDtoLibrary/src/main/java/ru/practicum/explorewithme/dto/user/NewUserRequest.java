package ru.practicum.explorewithme.dto.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Данные для создания нового пользователя
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NewUserRequest {
    @NotBlank
    public String name;
    @NotNull
    @Email
    public String email;
}
