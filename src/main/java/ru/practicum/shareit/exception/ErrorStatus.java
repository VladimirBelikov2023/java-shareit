package ru.practicum.shareit.exception;


public class ErrorStatus extends RuntimeException {

    public ErrorStatus(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorStatus() {

    }
}
