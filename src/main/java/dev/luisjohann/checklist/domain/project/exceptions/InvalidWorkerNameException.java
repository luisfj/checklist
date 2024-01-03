package dev.luisjohann.checklist.domain.project.exceptions;

import org.springframework.http.HttpStatus;

import dev.luisjohann.checklist.domain.exception.IChecklistException;

public class InvalidWorkerNameException extends IChecklistException {

    public InvalidWorkerNameException() {
        super("Worker name invalid", String.format("Worker name must be informed!"), HttpStatus.BAD_REQUEST);
    }

}
