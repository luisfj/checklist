package dev.luisjohann.checklist.infra.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import dev.luisjohann.checklist.domain.exception.IChecklistException;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ IChecklistException.class })
    public ResponseEntity<CheckListResponseException> handleIchecklistException(IChecklistException ex) {
        var responseException = new CheckListResponseException(ex.getTitle(), ex.getMessage());

        return new ResponseEntity<CheckListResponseException>(
                responseException, new HttpHeaders(), ex.getHttpStatus());
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<CheckListResponseException> handleExecptions(Exception ex) {
        var responseException = new CheckListResponseException("Exception", ex.getMessage());

        return new ResponseEntity<CheckListResponseException>(
                responseException, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
