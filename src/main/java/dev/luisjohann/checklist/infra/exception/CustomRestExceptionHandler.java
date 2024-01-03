package dev.luisjohann.checklist.infra.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import dev.luisjohann.checklist.domain.exception.IChecklistException;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ IChecklistException.class })
    public ResponseEntity<CheckListResponseException> handleAll(IChecklistException ex) {
        var responseException = new CheckListResponseException(ex.getTitle(), ex.getMessage());

        return new ResponseEntity<CheckListResponseException>(
                responseException, new HttpHeaders(), ex.getHttpStatus());
    }
}
