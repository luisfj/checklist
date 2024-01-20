package dev.luisjohann.checklist.domain.todo.exception;

import org.springframework.http.HttpStatus;

import dev.luisjohann.checklist.domain.exception.IChecklistException;

public class TodoRequiredException extends IChecklistException {

    public TodoRequiredException() {
        super("Todo is required",
                String.format("Todo is required!"),
                HttpStatus.BAD_REQUEST);
    }

}
