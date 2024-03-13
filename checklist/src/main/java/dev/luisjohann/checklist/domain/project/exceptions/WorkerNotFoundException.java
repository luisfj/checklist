package dev.luisjohann.checklist.domain.project.exceptions;

import org.springframework.http.HttpStatus;

import dev.luisjohann.checklist.domain.exception.IChecklistException;

public class WorkerNotFoundException extends IChecklistException {

    public WorkerNotFoundException(String workerNamer, String slug) {
        super("Worker not found",
                String.format("Worker with name: '%s' for de project with slug: '%s' not found!", workerNamer, slug),
                HttpStatus.NOT_FOUND);
    }

}
