package dev.luisjohann.checklist.domain.todo.exception;

import org.springframework.http.HttpStatus;

import dev.luisjohann.checklist.domain.exception.IChecklistException;

public class EmptyTodoTitleException extends IChecklistException {

    public EmptyTodoTitleException() {
        super("Title is required",
                String.format("Title cannot be empty!"),
                HttpStatus.BAD_REQUEST);
    }

}
