package dev.luisjohann.checklist.domain.project.exceptions;

import org.springframework.http.HttpStatus;

import dev.luisjohann.checklist.domain.IChecklistException;

public class ProjectRequiredException extends IChecklistException {

    public ProjectRequiredException() {
        super("Project is required", String.format("The project must be informed!"), HttpStatus.BAD_REQUEST);
    }

}
