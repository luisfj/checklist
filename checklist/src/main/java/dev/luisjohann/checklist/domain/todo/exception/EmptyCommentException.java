package dev.luisjohann.checklist.domain.todo.exception;

import org.springframework.http.HttpStatus;

import dev.luisjohann.checklist.domain.exception.IChecklistException;

public class EmptyCommentException extends IChecklistException {

    public EmptyCommentException() {
        super("Comment is required",
                String.format("Comment cannot be empty!"),
                HttpStatus.BAD_REQUEST);
    }

}
