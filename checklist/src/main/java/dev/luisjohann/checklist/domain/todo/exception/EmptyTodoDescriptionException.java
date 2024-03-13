package dev.luisjohann.checklist.domain.todo.exception;

import org.springframework.http.HttpStatus;

import dev.luisjohann.checklist.domain.exception.IChecklistException;

public class EmptyTodoDescriptionException extends IChecklistException {

    public EmptyTodoDescriptionException() {
        super("Description is required",
                String.format("Description cannot be empty!"),
                HttpStatus.BAD_REQUEST);
    }

}
