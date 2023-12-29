package dev.luisjohann.checklist.domain.todo.exception;

import org.springframework.http.HttpStatus;

import dev.luisjohann.checklist.domain.IChecklistException;

public class TodoNotFoundException extends IChecklistException {

    public TodoNotFoundException(String todoId, String projectSlug) {
        super("Todo not found",
                String.format("Todo with id: '%s' not found for the project with slug: '%s'!", todoId, projectSlug),
                HttpStatus.NOT_FOUND);
    }

}
