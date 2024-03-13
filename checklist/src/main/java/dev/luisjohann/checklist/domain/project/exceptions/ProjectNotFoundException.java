package dev.luisjohann.checklist.domain.project.exceptions;

import org.springframework.http.HttpStatus;

import dev.luisjohann.checklist.domain.exception.IChecklistException;

public class ProjectNotFoundException extends IChecklistException {

    public ProjectNotFoundException(String slug) {
        super("Project not found", String.format("Project with slug: '%s' not found!", slug), HttpStatus.NOT_FOUND);
    }

}
