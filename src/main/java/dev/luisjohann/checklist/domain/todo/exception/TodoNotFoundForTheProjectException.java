package dev.luisjohann.checklist.domain.todo.exception;

import org.springframework.http.HttpStatus;

import dev.luisjohann.checklist.domain.exception.IChecklistException;

public class TodoNotFoundForTheProjectException extends IChecklistException {

    public TodoNotFoundForTheProjectException(String slug) {
        super("Todo's not found",
                String.format("Todo list for de project with slug: '%s' is empty!", slug),
                HttpStatus.NOT_FOUND);
    }

}
