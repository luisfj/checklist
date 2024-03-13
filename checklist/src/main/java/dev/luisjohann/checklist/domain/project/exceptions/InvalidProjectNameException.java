package dev.luisjohann.checklist.domain.project.exceptions;

import org.springframework.http.HttpStatus;

import dev.luisjohann.checklist.domain.exception.IChecklistException;

public class InvalidProjectNameException extends IChecklistException {

    public InvalidProjectNameException() {
        super("Project name invalid", String.format("Project name must be informed!"), HttpStatus.BAD_REQUEST);
    }

}
