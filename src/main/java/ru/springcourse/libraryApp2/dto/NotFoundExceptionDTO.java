package ru.springcourse.libraryApp2.dto;

public class NotFoundExceptionDTO {
    private String message;


    public NotFoundExceptionDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
