package dev.luisjohann.checklist.domain.project.exceptions;

import org.springframework.http.HttpStatus;

import dev.luisjohann.checklist.domain.IChecklistException;

public class WorkerAlreadyExistsException extends IChecklistException {

    public WorkerAlreadyExistsException(String workerNamer, String slug) {
        super("Worker already exists",
                String.format("Worker with name: '%s' for the project with slug: '%s' already exists!", workerNamer,
                        slug),
                HttpStatus.BAD_REQUEST);
    }

}
