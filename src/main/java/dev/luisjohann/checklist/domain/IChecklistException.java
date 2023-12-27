package dev.luisjohann.checklist.domain;

import org.springframework.http.HttpStatus;

public abstract class IChecklistException extends RuntimeException {

    private String title;
    private HttpStatus httpStatus;

    public IChecklistException(String title, String message, HttpStatus httpStatus) {
        super(message);
        this.title = title;
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getTitle() {
        return title;
    }
}