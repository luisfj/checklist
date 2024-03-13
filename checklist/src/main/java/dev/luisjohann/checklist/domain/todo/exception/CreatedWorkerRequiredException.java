package dev.luisjohann.checklist.domain.todo.exception;

import org.springframework.http.HttpStatus;

import dev.luisjohann.checklist.domain.exception.IChecklistException;

public class CreatedWorkerRequiredException extends IChecklistException {

    public CreatedWorkerRequiredException() {
        super("Worker required",
                String.format("Created worker is required!"),
                HttpStatus.BAD_REQUEST);
    }

}
