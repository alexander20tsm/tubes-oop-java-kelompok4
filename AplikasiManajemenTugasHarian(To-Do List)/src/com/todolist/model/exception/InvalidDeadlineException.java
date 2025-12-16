package com.todolist.model.exception;

public class InvalidDeadlineException extends Exception {
    public InvalidDeadlineException(String message) {
        super(message);
    }
}