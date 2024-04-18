package dev.luisjohann.checklist.domain.project.exceptions;

import org.springframework.http.HttpStatus;

import dev.luisjohann.checklist.domain.exception.IChecklistException;

public class InvalidProjectDescriptionException extends IChecklistException {

    public InvalidProjectDescriptionException() {
        super("Project description to long", String.format("Project description must be lower 50 characters!"),
                HttpStatus.BAD_REQUEST);
    }

}
