package dev.luisjohann.checklist.domain.project.exceptions;

import org.springframework.http.HttpStatus;

import dev.luisjohann.checklist.domain.exception.IChecklistException;

public class WorkerRequiredException extends IChecklistException {

    public WorkerRequiredException() {
        super("Worker required",
                String.format("Worker is required!"),
                HttpStatus.BAD_REQUEST);
    }

}
