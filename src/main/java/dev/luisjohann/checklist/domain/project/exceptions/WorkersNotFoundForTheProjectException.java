package dev.luisjohann.checklist.domain.project.exceptions;

import org.springframework.http.HttpStatus;

import dev.luisjohann.checklist.domain.IChecklistException;

public class WorkersNotFoundForTheProjectException extends IChecklistException {

    public WorkersNotFoundForTheProjectException(String slug) {
        super("Workers not found",
                String.format("Worker list for de project with slug: '%s' is empty!", slug),
                HttpStatus.NOT_FOUND);
    }

}
