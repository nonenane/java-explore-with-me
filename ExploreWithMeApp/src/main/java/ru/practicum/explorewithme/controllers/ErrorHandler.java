package ru.practicum.explorewithme.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explorewithme.exceptions.*;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final BadRequestException e) {
        log.info("error: BadRequestException");
        return new ApiError(null,
                e.getMessage(),
                "For the requested operation the conditions are not met.",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleForbiddenException(final ForbiddenException e) {
        log.info("error: ForbiddenException");
        e.printStackTrace();
        return new ApiError(null,
                "Only pending or canceled events can be changed",
                "For the requested operation the conditions are not met.",
                HttpStatus.FORBIDDEN,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        log.info("error: NotFoundException");
        return new ApiError(null,
                e.getMessage(),
                "The required object was not found.",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(final ConflictException e) {
        log.info("error: ConflictException");
        return new ApiError(null,
                e.getMessage(),
                "Integrity constraint has been violated.",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(final Exception e) {
        e.printStackTrace();
        log.info("error: Exception");
        return new ApiError(null,
                e.getMessage(),
                "Unexpected error",
                HttpStatus.NOT_FOUND,
                LocalDateTime.now());
    }


}
