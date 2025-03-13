package ru.springcourse.libraryApp2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.springcourse.libraryApp2.dto.NotFoundExceptionDTO;

@ControllerAdvice
public class BookNotFoundExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Object> handleBookNotFoundException(BookNotFoundException ex) {
        return new ResponseEntity<>(new NotFoundExceptionDTO(ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
