package ru.practicum.explorewithme.controllers.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Set;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/users")
public class UserAdminController {

    private final UserService userService;

    public UserAdminController(UserService userService) {
        this.userService = userService;
    }

    //@ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public UserDto createUser(@Valid @RequestBody NewUserRequest newUserRequest) {

        UserDto userDto = userService.createUser(newUserRequest);
        log.info("Выполнен запрос createUser");
        return userDto;
    }

    @GetMapping()
    public List<UserDto> getUsers(@RequestParam(required = false) Set<Long> ids,
                                  @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                  @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {

        List<UserDto> dtoList = userService.getUsers(ids, from, size);
        log.info("Выполнен запрос getUsers");
        return dtoList;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId,
                           HttpServletRequest request) {

        userService.deleteUser(userId);
        log.info("Выполнен запрос deleteUser");
    }
}
