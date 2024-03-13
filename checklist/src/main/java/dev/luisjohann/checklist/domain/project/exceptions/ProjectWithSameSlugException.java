package dev.luisjohann.checklist.domain.project.exceptions;

import org.springframework.http.HttpStatus;

import dev.luisjohann.checklist.domain.exception.IChecklistException;

public class ProjectWithSameSlugException extends IChecklistException {

    public ProjectWithSameSlugException(String slug) {
        super("Project with slug exists", String.format("Project with slug: '%s' exists!", slug), HttpStatus.NOT_FOUND);
    }

}
