package dev.luisjohann.checklist.domain.todo.exception;

import org.springframework.http.HttpStatus;

import dev.luisjohann.checklist.domain.exception.IChecklistException;

public class CommentNotFoundException extends IChecklistException {

    public CommentNotFoundException(String id, String todoId) {
        super("Comment not found",
                String.format("Comment with id: '%s' not found for the TODO with id: '%s'!", id, todoId),
                HttpStatus.NOT_FOUND);
    }

}
