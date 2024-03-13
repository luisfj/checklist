package dev.luisjohann.checklist.domain.project.exceptions;

import org.springframework.http.HttpStatus;

import dev.luisjohann.checklist.domain.exception.IChecklistException;

public class WorkerWithSlugNotFoundException extends IChecklistException {

    public WorkerWithSlugNotFoundException(String workerSlug, String slug) {
        super("Worker not found",
                String.format("Worker with slug: '%s' for de project with slug: '%s' not found!", workerSlug, slug),
                HttpStatus.NOT_FOUND);
    }

}
